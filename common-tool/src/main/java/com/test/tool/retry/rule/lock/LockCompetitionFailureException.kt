package com.test.tool.retry.rule.lock

/**
 * 锁竞争失败异常
 *
 * @author 费世程
 * @date 2020/7/16 16:44
 */
class LockCompetitionFailureException : RuntimeException() {
  override val message: String?
    get() = "锁竞争失败！"
}