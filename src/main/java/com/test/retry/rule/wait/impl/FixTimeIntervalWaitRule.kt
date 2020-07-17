package com.test.retry.rule.wait.impl

import com.test.retry.rule.wait.WaitRule

/**
 * 固定时长等待策略
 * @author 费世程
 * @date 2020/7/16 19:12
 */
class FixTimeIntervalWaitRule(private val timeInterval: Long) : WaitRule {


  init {
    require(timeInterval > 0) { "等待时间间隔应大于0ms" }
  }

  override fun delayMillis(): Long {
    return timeInterval
  }

}