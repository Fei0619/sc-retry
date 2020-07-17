package com.test.retry.wait.impl

import com.test.retry.wait.WaitRule

/**
 * 不等待
 * @author 费世程
 * @date 2020/7/16 19:02
 */
object NoWaitRule : WaitRule {

  override fun delayMillis(): Long {
    return 0L
  }
}