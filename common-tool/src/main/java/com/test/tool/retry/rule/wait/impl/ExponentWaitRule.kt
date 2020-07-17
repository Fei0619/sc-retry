package com.test.tool.retry.rule.wait.impl

import com.test.tool.retry.rule.wait.WaitRule

/**
 * 指数递增时长等待策略
 * @author 费世程
 * @date 2020/7/16 19:44
 */
class ExponentWaitRule(private val initial: Long, private val exponent: Int) : WaitRule {

  init {
    require(initial > 0) { "初始等待时长应大于0ms" }
    require(exponent > 0) { "指数应大于0" }
  }

  private var currentDelay = -1L

  override fun delayMillis(): Long {
    return if (currentDelay == -1L) {
      currentDelay = initial
      initial
    } else {
      currentDelay *= exponent
      currentDelay
    }
  }

}