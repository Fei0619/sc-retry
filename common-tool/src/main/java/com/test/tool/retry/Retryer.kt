package com.test.tool.retry

import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.stop.StopRule
import com.test.tool.retry.rule.wait.WaitRule
import java.util.concurrent.Callable

/**
 * 重试器
 * @author 费世程
 * @date 2020/7/16 16:23
 */
interface Retryer<R, FR> {

  fun lockRule(lockRule: LockRule<*>): Retryer<R, FR>

  fun stopRule(stopRule: StopRule): Retryer<R, FR>

  fun waitRule(waitRule: WaitRule): Retryer<R, FR>

  fun conditions(vararg exceptions: Throwable): Retryer<R, FR>

  fun task(task: Callable<R>): Retryer<R, FR>

  fun task(task: () -> R): Retryer<R, FR>
}