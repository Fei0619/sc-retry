package com.test.tool.retry.rule.lock.impl

import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.lock.LockTypeEnum
import java.util.concurrent.locks.Lock

/**
 * 普通锁策略
 *
 * @author 费世程
 * @date 2020/7/16 16:36
 */
class GeneralLockRule internal constructor(lock: Lock) : LockRule<Lock> {

  override fun getLock(): Lock {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getLockType(): LockTypeEnum {
    return LockTypeEnum.GENERAL
  }
}