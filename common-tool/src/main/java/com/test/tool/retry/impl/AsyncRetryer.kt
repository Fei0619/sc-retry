package com.test.tool.retry.impl

import com.test.tool.init.Destroy
import com.test.tool.init.Initialize
import com.test.tool.retry.AbstractRetryer
import com.test.tool.retry.rule.lock.LockCompetitionFailureException
import com.test.tool.retry.rule.lock.LockTypeEnum
import com.test.tool.retry.rule.lock.impl.DistributeLockRule
import com.test.tool.retry.rule.lock.impl.GeneralLockRule
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import java.time.Duration
import java.util.concurrent.*
import java.util.concurrent.locks.Lock

/**
 * 异步重试器
 *
 * @author 费世程
 * @date 2020/7/16 20:00
 */
class AsyncRetryer<R> internal constructor(private var executorService: ExecutorService? = Executors.newSingleThreadExecutor())
  : AbstractRetryer<R, Future<R>>(), RunnableFuture<R>, Delayed {

  private val log = LoggerFactory.getLogger(AsyncRetryer::class.java)

  private val countDownLatch = CountDownLatch(1)
  /** 是否被锁 */
  private var locked = true
  /** 分布式锁key */
  private var lockKey: String? = null
  /** 分布式锁value */
  private var lockValue: String? = null
  /** 下一次执行时间 */
  private var nextExecuteTime: Long = -1L
  /** 已重试次数 */
  private var attemptTimes = 0
  /** 分布式锁超时时间 */
  private var dLockTimeout = Duration.ZERO
  /** 是否释放锁 */
  private var unlock = false
  /** 普通锁 */
  var lock: Lock? = null
  /** 执行过程中抛出的异常 */
  private var exception: Exception? = null
  /** 执行结果 */
  private var result: R? = null

  private var executeTimes = 0

  override fun execute(): Future<R> {
    require(task != null) { "执行任务不能为空！" }
    require(executeTimes++ == 0) { "不能重复执行！" }
    when (lockRule.getLockType()) {
      //不加锁
      LockTypeEnum.NONE -> {
        locked = false
      }
      //普通锁
      LockTypeEnum.GENERAL -> {
        val rule = lockRule as GeneralLockRule
        lock = rule.getLock()
        locked = !lock!!.tryLock()
      }
      //分布式锁
      LockTypeEnum.DISTRIBUTED -> {
        require(dLock != null) { "dLock is null" }
        require(cache != null) { "cache is null" }
        val rule = lockRule as DistributeLockRule
        lockKey = rule.getLock().first
        lockValue = rule.getLock().second
        val tryLock = dLock!!.tryLock(lockKey!!, lockValue!!, rule.maxExecuteTime).block()
        locked = true != tryLock
      }
    }
    if (locked) {
      //没竞争到锁,放弃执行并抛出锁竞争失败异常
      countDownLatch.countDown()
      throw LockCompetitionFailureException()
    } else {
      //竞争到锁
      doExecute()
    }
    return this
  }

  private fun doExecute() {
    executorService!!.execute(this)
  }

  override fun isDone(): Boolean {
    return countDownLatch.count == 0L
  }

  override fun get(): R {
    countDownLatch.await()
    return getResult()
  }

  override fun get(timeout: Long, unit: TimeUnit): R {
    countDownLatch.await(timeout, unit)
    return getResult()
  }

  private fun getResult(): R {
    if (exception != null) {
      throw exception!!
    }
    return result!!
  }

  override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
    throw UnsupportedOperationException("not support!")
  }

  override fun isCancelled(): Boolean {
    return false
  }

  /** 剩余延迟时间 */
  override fun getDelay(unit: TimeUnit): Long {
    if (nextExecuteTime < 0) {
      throw RuntimeException("下一次执行时间小于0")
    }
    return unit.convert(nextExecuteTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
  }

  override fun compareTo(other: Delayed?): Int {
    return (this.getDelay(TimeUnit.MILLISECONDS) - other!!.getDelay(TimeUnit.MILLISECONDS)).toInt()
  }

  override fun run() {
    val delay = waitRule.delayMillis()
    try {
      if (lockKey != null && lockValue != null) {
        //使用了分布式锁，给锁续期
        calculateDelayMillis(delay)
        cache!!.expire(lockKey!!, dLockTimeout!!).block()
      }
      result = this.task!!.call()
      resloveResource()
    } catch (e: Exception) {
      var needAttempt = false
      for (it in conditions) {
        if (it.isAssignableFrom(e::class.java)) {
          needAttempt = true
          break
        }
      }
      if (needAttempt) {
        if (failureFallBack != null) {
          failureFallBack?.accept(attemptTimes, e)
        }
        if (stopRule.stopRetry()) {
          //停止重试
          log.debug("已达失败重试上限，重试次数 --> $attemptTimes")
          if (finalFailureFallBack != null) {
            finalFailureFallBack!!.call()
          } else {
            exception = e
          }
          resloveResource()
        } else {
          attemptTimes += 1
          if (delay > 0) {
            log.debug("将在${delay}ms后执行第${attemptTimes}次尝试...")
            nextExecuteTime = System.currentTimeMillis() + delay
            AsyncDelayQueue.delayQueue.offer(this)
            return
          } else {
            log.debug("立即进行第${attemptTimes}次尝试...")
            doExecute()
            return
          }
        }
      } else {
        //异常类型不在conditions枚举中，无需重试
        resloveResource()
        exception = e
        throw e
      }
    } finally {
      if (unlock) {
        //释放锁
        unlock()
      }
    }
  }

  private fun resloveResource() {
    countDownLatch.countDown()
    unlock = true
    executorService = null
  }

  private fun unlock() {
    lock?.unlock()
    if (lockKey != null && lockValue != null) {
      dLock!!.unlock(lockKey!!, lockValue!!)
    }
  }

  /**
   * 计算本次分布式锁超时时间
   */
  private fun calculateDelayMillis(delayMillis: Long) {
    if (LockTypeEnum.DISTRIBUTED == lockRule.getLockType()) {
      val lock = lockRule as DistributeLockRule
      dLockTimeout = Duration.ofMillis(delayMillis + lock.maxExecuteTime.toMillis() + 5000L)
    }
  }

  /**
   * 延时队列
   */
  internal object AsyncDelayQueue : Initialize, Destroy {
    internal val delayQueue = DelayQueue<AsyncRetryer<*>>()
    @Volatile
    private var flag = true
    private var executeThread: Thread? = null


    override fun init() {
      while (flag) {
        executeThread = Thread {
          if (delayQueue.size > 0) {
            delayQueue.poll(3, TimeUnit.SECONDS)?.execute()
          }
        }
        executeThread!!.start()
      }
    }

    override fun toDestroy() {
      flag = false
    }
  }

}