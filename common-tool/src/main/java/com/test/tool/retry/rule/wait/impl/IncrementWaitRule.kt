package com.test.tool.retry.rule.wait.impl

import com.test.tool.retry.rule.wait.WaitRule
import java.time.Duration

/**
 * 递增时长等待策略
 * @author 费世程
 * @date 2020/7/16 19:14
 */
class IncrementWaitRule(private val initial: Duration, private val step: Duration) : WaitRule {

  init {
    require(initial.toMillis() > 0) { "初始等待时长应大于0ms" }
    require(step.toMillis() > 0) { "等待步长应大于0" }
  }

  private var currentDelay: Long = -1L

  override fun delayMillis(): Long {
    return if (currentDelay == -1L) {
      currentDelay = initial.toMillis()
      initial.toMillis()
    } else {
      currentDelay += step.toMillis()
      currentDelay
    }
  }

}