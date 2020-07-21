package com.test.tool.retry.impl

import com.test.tool.retry.AbstractRetryer
import com.test.tool.retry.rule.lock.LockCompetitionFailureException
import com.test.tool.retry.rule.lock.LockTypeEnum
import com.test.tool.retry.rule.lock.impl.DistributeLockRule
import com.test.tool.retry.rule.lock.impl.GeneralLockRule
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

/**
 * 阻塞重试器
 *
 * @author 费世程
 * @date 2020/7/17 16:21
 */
class BlockRetryer<R> internal constructor() : AbstractRetryer<R, Optional<R>>() {

  private val log = LoggerFactory.getLogger(BlockRetryer::class.java)

  private var executeTimes = 0
  /** 是否被锁 */
  private var locked = true
  private var lock: Lock? = null
  private var lockKey: String? = null
  private var lockValue: String? = null
  private val countDownLatch = CountDownLatch(1)
  private var dLockTimeout = Duration.ZERO
  private var attemptTimes = 0


  /** 竞争锁 */
  override fun execute(): Optional<R> {
    require(task != null) { "执行任务不能为空！" }
    require(executeTimes++ == 0) { "不能重复执行！" }
    when (lockRule.getLockType()) {
      LockTypeEnum.NONE -> {
        locked = false
      }
      LockTypeEnum.GENERAL -> {
        val rule = lockRule as GeneralLockRule
        lock = rule.getLock()
        locked = !lock!!.tryLock()
      }
      LockTypeEnum.DISTRIBUTED -> {
        require(dLock != null) { "dLock is null" }
        require(cache != null) { "cache is null" }
        val rule = lockRule as DistributeLockRule
        lockKey = rule.getLock().first
        lockValue = rule.getLock().second
        val tryLock = dLock!!.tryLock(lockKey!!, lockValue!!, Duration.ofSeconds(5)).block()
        locked = !tryLock!!
      }
    }
    if (locked) {
      //锁竞争失败
      countDownLatch.countDown()
      throw LockCompetitionFailureException()
    } else {
      try {
        return doExecute()
      } finally {
        unLock()
      }
    }
  }

  /** 竞争到锁，处理业务 */
  private fun doExecute(): Optional<R> {
    val delayMillis = waitRule.delayMillis()
    try {
      //分布式锁，对锁进行续期
      if (lockKey != null && lockValue != null) {
        calculateDlockTimeout(delayMillis)
        cache!!.expire(lockKey!!, dLockTimeout).block()
      }
      return Optional.ofNullable(task!!.call())
    } catch (e: Exception) {
      var needRetry = false
      for (it in conditions) {
        if (it.isAssignableFrom(e::class.java)) {
          needRetry = true
          break
        }
      }
      if (needRetry) {
        failureFallBack?.accept(attemptTimes, e)
        if (stopRule.stopRetry()) {
          //已达失败重试上限
          log.debug("已达重试上限，放弃重试，重试次数 -> $attemptTimes")
          if (finalFailureFallBack != null) {
            return Optional.ofNullable(finalFailureFallBack!!.call())
          } else {
            throw e
          }
        } else {
          attemptTimes++
          if (delayMillis > 0) {
            log.debug("即将在${delayMillis}ms后进行第${attemptTimes}次重试...")
            TimeUnit.MILLISECONDS.sleep(delayMillis)
          } else {
            log.debug("立即进行重试...")
          }
          return doExecute()
        }
      } else {
        throw e
      }
    }
  }

  /** 释放锁 */
  private fun unLock() {
    lock?.unlock()
    if (lockKey != null && lockValue != null) {
      dLock!!.unlock(lockKey!!, lockValue!!).block()
    }
  }

  /** 计算分布式锁超时时间 */
  private fun calculateDlockTimeout(delayMillis: Long) {
    if (LockTypeEnum.DISTRIBUTED == lockRule.getLockType()) {
      val distributeLockRule = lockRule as DistributeLockRule
      dLockTimeout = Duration.ofMillis(delayMillis + distributeLockRule.maxExecuteTime.toMillis() + 5000)
    }
  }

}