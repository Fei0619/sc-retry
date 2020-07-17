package com.test.retry.rule.lock

/**
 * @author 费世程
 * @date 2020/7/16 16:29
 */
enum class LockTypeEnum {

  /**
   * 不加锁
   */
  NONE,
  /**
   * 普通锁
   */
  GENERAL,
  /**
   * 分布式锁
   */
  DISTRIBUTED,
  ;

}