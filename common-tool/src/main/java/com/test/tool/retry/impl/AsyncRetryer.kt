package com.test.tool.retry.impl

import com.test.tool.retry.AbstractRetryer
import com.test.tool.retry.Retryer
import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.stop.StopRule
import com.test.tool.retry.rule.wait.WaitRule
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * 异步重试器
 *
 * @author 费世程
 * @date 2020/7/16 20:00
 */
class AsyncRetryer<R> internal constructor() : AbstractRetryer<R, Future<R>>() {

  override fun lockRule(lockRule: LockRule<*>): Retryer<R, Future<R>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun stopRule(stopRule: StopRule): Retryer<R, Future<R>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun waitRule(waitRule: WaitRule): Retryer<R, Future<R>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun conditions(vararg exceptions: Throwable): Retryer<R, Future<R>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun task(task: Callable<R>): Retryer<R, Future<R>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun task(task: () -> R): Retryer<R, Future<R>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}