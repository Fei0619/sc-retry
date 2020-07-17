package com.test.retry.rule.lock

import com.test.retry.rule.lock.impl.DistributeLockRule
import com.test.retry.rule.lock.impl.GeneralLockRule
import com.test.retry.rule.lock.impl.NoneLockRule
import java.time.Duration
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * 锁策略
 *
 * @author 费世程
 * @date 2020/7/16 16:24
 */
interface LockRule<L> {

  companion object {
    /**
     * 没有锁
     */
    fun noneLock(): LockRule<String> {
      return NoneLockRule
    }

    /**
     * 普通锁
     */
    fun generalLock(lock: Lock = ReentrantLock()): LockRule<Lock> {
      return GeneralLockRule(lock)
    }

    /**
     * 分布式锁
     * @param lockKey lockKey
     * @param maxExecuteTime 预估最大执行时间
     */
    fun distributeLock(lockKey: String, maxExecuteTime: Duration = Duration.ofSeconds(5))
        : LockRule<Pair<String, String>> {
      val lockValue = UUID.randomUUID().toString()
      return DistributeLockRule(lockKey to lockValue, maxExecuteTime)
    }

  }

  /**
   * 获取锁
   */
  fun getLock(): L

  /**
   * 获取锁类型
   */
  fun getLockType(): LockTypeEnum

}