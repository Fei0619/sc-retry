package com.test.retry.rule.stop.impl

import com.test.retry.rule.stop.StopRule
import java.time.Duration

/**
 * 【maxWaitTime】后停止
 * @author 费世程
 * @date 2020/7/16 18:41
 */
class StopAfterMaxWaitTimeRule(maxWaitTime: Duration) : StopRule {

  /**
   * 停止重试时间
   */
  private val stopTime: Long

  init {
    val delayTime = maxWaitTime.toHours()
    require(delayTime > 1) { "等待时间至少为1ms" }
    stopTime = System.currentTimeMillis() + maxWaitTime.toMillis()
  }

  override fun stopRetry(): Boolean {
    return System.currentTimeMillis() > stopTime
  }

}