package com.test.tool.retry.impl

import com.test.tool.retry.AbstractRetryer
import com.test.tool.retry.rule.lock.LockCompetitionFailureException
import com.test.tool.retry.rule.lock.LockTypeEnum
import com.test.tool.retry.rule.lock.impl.DistributeLockRule
import com.test.tool.retry.rule.lock.impl.GeneralLockRule
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.locks.Lock

/**
 * 反应式重试器
 *
 * @author 费世程
 * @date 2020/7/17 16:22
 */
class ReactiveRetryer<R> internal constructor() : AbstractRetryer<Mono<R>, Mono<R?>>() {

  private val log = LoggerFactory.getLogger(ReactiveRetryer::class.java)
  private var lock: Lock? = null
  private var dLockTimeout = Duration.ZERO
  private var attemptTimes = 0

  override fun execute(): Mono<R?> {
    return doExecute().doOnError {
      if (it is LockCompetitionFailureException) {
        log.debug("锁竞争失败...")
      }
    }
  }

  private fun doExecute(): Mono<R?> {
    when (lockRule.getLockType()) {
      LockTypeEnum.NONE -> {
        //没加锁，直接执行
        return doExecute(null)
      }
      LockTypeEnum.GENERAL -> {
        val generalLockRule = lockRule as GeneralLockRule
        lock = generalLockRule.getLock()
        return if (lock!!.tryLock()) {
          doExecute(null).doFinally {
            lock!!.unlock()
          }
        } else {
          Mono.error(LockCompetitionFailureException())
        }
      }
      LockTypeEnum.DISTRIBUTED -> {
        require(dLock != null) { "dLock is null.." }
        require(cache != null) { "cache is null.." }
        val delayMillis = waitRule.delayMillis()
        calculate(delayMillis)
        val distributeLockRule = lockRule as DistributeLockRule
        val lockKey = distributeLockRule.getLock().first
        val lockValue = distributeLockRule.getLock().second
        return dLock!!.tryLock(lockKey, lockValue, dLockTimeout).flatMap {
          if (it) {
            doExecute(lockKey).doFinally {
              dLock!!.unlock(lockKey, lockValue)
            }
          } else {
            Mono.error(LockCompetitionFailureException())
          }
        }
      }
    }
  }

  /**
   * @param lockKey 分布式锁key
   * @return Mono<R>
   */
  private fun doExecute(lockKey: String?): Mono<R?> {
    require(task != null) { "任务不能为空！" }
    return task!!.call().onErrorResume { throwable ->
      var needRetry = false
      for (con in conditions) {
        if (con.isAssignableFrom(throwable::class.java)) {
          needRetry = true
          break
        }
      }
      if (needRetry) {
        failureFallBack?.accept(attemptTimes, throwable)
        if (stopRule.stopRetry()) {
          //已达重试上限
          if (finalFailureFallBack != null) {
            finalFailureFallBack!!.call()
          } else {
            Mono.error(throwable)
          }
        } else {
          attemptTimes++
          val delayMillis = waitRule.delayMillis()
          if (delayMillis > 0) {
            log.debug("$delayMillis ms -> 后将重试...")
            //需要延迟
            Mono.delay(Duration.ofMillis(delayMillis)).flatMap {
              if (lockKey != null) {
                calculate(delayMillis)
                //设置了分布式锁，需要对锁进行续期
                cache!!.expire(lockKey, dLockTimeout).flatMap {
                  doExecute(lockKey)
                }
              } else {
                doExecute(lockKey)
              }
            }
          } else {
            log.debug("即将进行第${attemptTimes}次重试...")
            if (lockKey != null) {
              calculate(delayMillis)
              cache!!.expire(lockKey, dLockTimeout).flatMap {
                doExecute(lockKey)
              }
            } else {
              doExecute(lockKey)
            }
          }
        }
      } else {
        //不需要重试
        Mono.error(throwable)
      }
    }
  }

  private fun calculate(delayMillis: Long) {
    if (LockTypeEnum.DISTRIBUTED == lockRule.getLockType()) {
      val distributeLockRule = lockRule as DistributeLockRule
      val maxExecuteTime = distributeLockRule.maxExecuteTime
      dLockTimeout = Duration.ofMillis(delayMillis + maxExecuteTime.toMillis() + 5000)
    }
  }

}