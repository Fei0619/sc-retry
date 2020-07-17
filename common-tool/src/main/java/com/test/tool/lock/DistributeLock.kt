package com.test.tool.lock

import reactor.core.publisher.Mono
import java.time.Duration

/**
 * @author 费世程
 * @date 2020/7/17 9:44
 */
interface DistributeLock {

  fun tryLock(lockKey: String, lockValue: String, expires: Duration): Mono<Boolean>

  fun tryLock(lockKey: String, lockValue: String, expires: Duration, timeOut: Duration): Mono<Boolean>

  fun unlock(lockKey: String, lockValue: String): Mono<Boolean>

  fun forceUnLock(lockKey: String): Mono<Boolean>

}