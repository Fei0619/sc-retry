package com.test.retry.stop.impl

import com.test.retry.stop.StopRule

/**
 * 不停止，一直重试
 *
 * @author 费世程
 * @date 2020/7/16 17:25
 */
object NeverStopRule : StopRule {

  override fun stopRetry(): Boolean {
    return false
  }
}