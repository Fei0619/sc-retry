package com.test.retry.rule.lock.impl

import com.test.retry.rule.lock.LockRule
import com.test.retry.rule.lock.LockTypeEnum
import java.time.Duration

/**
 * 分布式锁策略
 *
 * @author 费世程
 * @date 2020/7/16 16:36
 */
class DistributeLockRule internal constructor(
    /**
     * 分布式锁信息：第一个为lockKey，第二个为lockValue
     */
    pair: Pair<String, String>,
    /**
     * 预估最大执行时间
     */
    maxExecuteTime: Duration
) : LockRule<Pair<String, String>> {

  override fun getLock(): Pair<String, String> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getLockType(): LockTypeEnum {
    return LockTypeEnum.DISTRIBUTED
  }
}