package com.test.instance.controller.retry

import com.test.tool.common.Res
import com.test.tool.retry.Retryer
import com.test.tool.retry.rule.lock.LockRule
import com.test.tool.retry.rule.stop.StopRule
import com.test.tool.retry.rule.wait.WaitRule
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * @author 费世程
 * @date 2020/7/20 18:58
 */
@RestController
@RequestMapping("/retry")
class RetryController {

  private val ZERO = 0

  /**
   * 分布式锁
   */
  @GetMapping("/async")
  fun asyncRetyer(): Res<*> {
    Retryer.asyncRetyer<Unit>(Executors.newSingleThreadExecutor())
        .lockRule(LockRule.noneLock())
        .waitRule(WaitRule.fixTimeIntervalWait(Duration.ofSeconds(3)))
        .stopRule(StopRule.stopAfterMaxAttempt(3))
        .conditions(Throwable::class.java)
        .failureFallback { i, t -> System.err.println("${System.currentTimeMillis()} -> 第 $i 次重试失败，message -> ${t.message}") }
        .finalFailureFallBack(Callable { System.err.println("执行失败...") })
        .execute { 1 / ZERO }
    return Res.success("执行完成")
  }

  /**
   * 反应式锁
   */
  @GetMapping("/reactive")
  fun reactiveRetryer(): Res<*> {
    Retryer.reactiveRetryer<Unit>()
        .lockRule(LockRule.noneLock())
        .stopRule(StopRule.stopAfterMaxAttempt(3))
        .waitRule(WaitRule.exponentWait(2000, 2))
        .conditions(Throwable::class.java)
        .failureFallback { i, t -> System.err.println("第 $i 次重试失败，message -> ${t.message}") }
        .execute { Mono.just(System.err.println(18 / ZERO)) }
    return Res.success("执行完成")
  }

}

fun main() {
  /**
   * 阻塞锁
   */
  Retryer.blockRetyer<Unit>().conditions(Throwable::class.java)
      .lockRule(LockRule.noneLock())
//      .lockRule(LockRule.distributeLock("distribute", Duration.ofSeconds(3)))
      .waitRule(WaitRule.fixTimeIntervalWait(Duration.ofSeconds(3)))
      .stopRule(StopRule.stopAfterMaxAttempt(3))
      .failureFallback { i, t -> System.err.println("第 $i 次重试失败，message -> ${t.message}") }
      .finalFailureFallBack { System.err.println("执行失败...") }
      .execute { 1 / 0 }
}
