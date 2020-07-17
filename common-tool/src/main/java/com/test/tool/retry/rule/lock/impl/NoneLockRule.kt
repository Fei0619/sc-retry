package com.test.tool.retry.rule.lock.impl

import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.lock.LockTypeEnum

/**
 * 不加锁
 *
 * @author 费世程
 * @date 2020/7/16 16:34
 */
object NoneLockRule : LockRule<String> {

  override fun getLock(): String {
    return ""
  }

  override fun getLockType(): LockTypeEnum {
    return LockTypeEnum.NONE
  }
}