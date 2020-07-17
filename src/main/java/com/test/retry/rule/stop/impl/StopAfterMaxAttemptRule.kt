package com.test.retry.rule.stop.impl

import com.test.retry.rule.stop.StopRule

/**
 * 重试【maxAttemptTimes】次后停止
 *
 * @author 费世程
 * @date 2020/7/16 18:05
 */
class StopAfterMaxAttemptRule(private val maxAttemptTimes: Int) : StopRule {

  /**
   * 当前已重试次数
   */
  private var count = 0

  override fun stopRetry(): Boolean {
    return count++ > maxAttemptTimes
  }

}