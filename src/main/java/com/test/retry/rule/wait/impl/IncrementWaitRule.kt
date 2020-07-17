package com.test.retry.rule.wait.impl

import com.test.retry.rule.wait.WaitRule

/**
 * 递增时长等待策略
 * @author 费世程
 * @date 2020/7/16 19:14
 */
class IncrementWaitRule(private val initial: Long, private val step: Long) : WaitRule {

  init {
    require(initial > 0) { "初始等待时长应大于0ms" }
    require(step > 0) { "等待步长应大于0" }
  }

  private var currentDelay: Long = -1L

  override fun delayMillis(): Long {
    return if (currentDelay == -1L) {
      currentDelay = initial
      initial
    } else {
      currentDelay += step
      currentDelay
    }
  }

}