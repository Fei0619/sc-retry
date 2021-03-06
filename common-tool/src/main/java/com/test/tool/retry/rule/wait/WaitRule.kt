package com.test.tool.retry.rule.wait

import com.test.tool.retry.rule.wait.impl.*
import java.time.Duration

/**
 * 等待策略
 *
 * @author 费世程
 * @date 2020/7/16 16:29
 */
interface WaitRule {

  companion object {
    /**
     * 不等待
     */
    fun noWait(): WaitRule {
      return NoWaitRule
    }

    /**
     * 固定时长等待策略
     */
    fun fixTimeIntervalWait(timeInterval: Duration): WaitRule {
      return FixTimeIntervalWaitRule(timeInterval)
    }

    /**
     * 递增时长等待策略
     */
    fun incrementWait(initial: Duration, step: Duration): WaitRule {
      return IncrementWaitRule(initial, step)
    }

    /**
     * 随机等待时长策略
     */
    fun randomWait(upperMillis: Long, lowerMillis: Long): WaitRule {
      return RandomWaitRule(upperMillis, lowerMillis)
    }

    /**
     * 指数递增时长等待策略
     */
    fun exponentWait(initial: Long, exponent: Int): WaitRule {
      return ExponentWaitRule(initial, exponent)
    }

  }

  /**
   * 获取重试时间间隔:ms
   */
  fun delayMillis(): Long

}