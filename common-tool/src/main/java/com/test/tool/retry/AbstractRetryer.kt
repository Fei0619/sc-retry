package com.test.tool.retry

import com.test.tool.cache.ReactiveRedisCache
import com.test.tool.lock.DistributeLock
import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.stop.StopRule
import com.test.tool.retry.rule.wait.WaitRule
import java.time.Duration
import java.util.concurrent.Callable
import java.util.concurrent.locks.Lock
import java.util.function.BiConsumer

/**
 * @author 费世程
 * @date 2020/7/16 19:55
 */
abstract class AbstractRetryer<R, FR> : Retryer<R, FR> {

  companion object {
    private val conditionList: Array<out Class<out Throwable>> = arrayOf(Throwable::class.java)
    /** 默认最大重试次数 */
    private const val attemptTimes = 3
    private val waitTime = Duration.ofSeconds(3)

    /** 分布式锁 */
    var dLock: DistributeLock? = null
    var cache: ReactiveRedisCache? = null

  }

  protected var lockRule: LockRule<*> = LockRule.noneLock()
  protected var stopRule: StopRule = StopRule.stopAfterMaxAttempt(attemptTimes)
  protected var waitRule: WaitRule = WaitRule.fixTimeIntervalWait(waitTime)
  protected var conditions: Array<out Class<out Throwable>> = conditionList
  protected var task: Callable<R>? = null
  protected var failureFallBack: BiConsumer<Int, Throwable>? = null
  protected var finalFailureFallBack: Callable<R>? = null

  override fun lockRule(lockRule: LockRule<*>): Retryer<R, FR> {
    this.lockRule = lockRule
    return this
  }

  override fun stopRule(stopRule: StopRule): Retryer<R, FR> {
    this.stopRule = stopRule
    return this
  }

  override fun waitRule(waitRule: WaitRule): Retryer<R, FR> {
    this.waitRule = waitRule
    return this
  }

  override fun conditions(vararg conditions: Class<out Throwable>): Retryer<R, FR> {
    this.conditions = conditions
    return this
  }

  override fun failureFallback(fallback: (i: Int, t: Throwable) -> Unit): Retryer<R, FR> {
    this.failureFallBack = BiConsumer(fallback)
    return this
  }

  override fun finalFailureFallBack(fallback: Callable<R>): Retryer<R, FR> {
    this.finalFailureFallBack = fallback
    return this
  }

  override fun finalFailureFallBack(fallback: () -> R): Retryer<R, FR> {
    this.finalFailureFallBack = Callable(fallback)
    return this
  }

  override fun task(task: Callable<R>): Retryer<R, FR> {
    this.task = task
    return this
  }

  override fun task(task: () -> R): Retryer<R, FR> {
    this.task = Callable(task)
    return this
  }

  override fun execute(task: Callable<R>): FR {
    this.task = task
    return execute()
  }

  override fun execute(task: () -> R): FR {
    this.task = Callable(task)
    return execute()
  }
}