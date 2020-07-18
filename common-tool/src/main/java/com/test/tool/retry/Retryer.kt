package com.test.tool.retry

import com.test.tool.retry.impl.AsyncRetryer
import com.test.tool.retry.impl.BlockRetryer
import com.test.tool.retry.impl.ReactiveRetryer
import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.stop.StopRule
import com.test.tool.retry.rule.wait.WaitRule
import reactor.core.publisher.Mono
import java.awt.dnd.DropTargetAdapter
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * 重试器
 * @author 费世程
 * @date 2020/7/16 16:23
 */
interface Retryer<R, FR> {

  companion object {
    /** 异步重试器 */
    fun <R> asyncRetyer(executorService: ExecutorService): Retryer<R, Future<R>> {
      return AsyncRetryer(executorService)
    }

    /** 阻塞重试器 */
    fun <R> blockRetyer(): Retryer<R, Optional<R>> {
      return BlockRetryer()
    }

    /** 反应式重试器 */
    fun <R> reactiveRetryer(): Retryer<R, Mono<R>> {
      return ReactiveRetryer()
    }
  }

  /** 设置锁规则 */
  fun lockRule(lockRule: LockRule<*>): Retryer<R, FR>

  /** 设置停止重试规则 */
  fun stopRule(stopRule: StopRule): Retryer<R, FR>

  /** 设置等待规则 */
  fun waitRule(waitRule: WaitRule): Retryer<R, FR>

  /** 设置重试条件 */
  fun conditions(vararg conditions: Class<out Throwable>): Retryer<R, FR>

  /** 重试失败回调 */
  fun failureFallback(fallback: (i: Int, t: Throwable) -> Unit): Retryer<R, FR>

  /** 最终失败回调 */
  fun finalFailureFallBack(fallback: Callable<R>): Retryer<R, FR>

  /** 最终失败回调 */
  fun finalFailureFallBack(fallback: () -> R): Retryer<R, FR>

  /** 设置任务 */
  fun task(task: Callable<R>): Retryer<R, FR>

  /** 设置任务 */
  fun task(task: () -> R): Retryer<R, FR>

  /** 设置任务并执行 */
  fun execute(task: Callable<R>): FR

  /** 设置任务并执行 */
  fun execute(task: () -> R): FR

  /** 执行 */
  fun execute(): FR

}