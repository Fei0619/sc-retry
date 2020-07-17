package com.test.retry.stop

import com.test.retry.stop.impl.NeverStopRule
import com.test.retry.stop.impl.StopAfterMaxAttemptRule
import com.test.retry.stop.impl.StopAfterMaxWaitTimeRule
import java.time.Duration

/**
 * 停止重试策略
 *
 * @author 费世程
 * @date 2020/7/16 16:28
 */
interface StopRule {

  companion object {

    fun neverStop(): StopRule {
      return NeverStopRule
    }

    /**
     * 重试【maxAttemptTimes】次
     */
    fun stopAfterMaxAttempt(maxAttemptTimes: Int): StopRule {
      return StopAfterMaxAttemptRule(maxAttemptTimes)
    }

    /**
     * 等待【maxWaitTime】时间
     */
    fun stopAfterMaxWaitTime(maxWaitTime: Duration): StopRule {
      return StopAfterMaxWaitTimeRule(maxWaitTime)
    }
  }

  fun stopRetry(): Boolean

}