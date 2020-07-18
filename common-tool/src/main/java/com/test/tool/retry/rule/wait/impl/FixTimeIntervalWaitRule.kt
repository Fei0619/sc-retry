package com.test.tool.retry.rule.wait.impl

import com.test.tool.retry.rule.wait.WaitRule
import java.time.Duration

/**
 * 固定时长等待策略
 * @author 费世程
 * @date 2020/7/16 19:12
 */
class FixTimeIntervalWaitRule(private val timeInterval: Duration) : WaitRule {

  init {
    require(timeInterval.toMillis() > 0) { "等待时间间隔应大于0ms" }
  }

  override fun delayMillis(): Long {
    return timeInterval.toMillis()
  }

}