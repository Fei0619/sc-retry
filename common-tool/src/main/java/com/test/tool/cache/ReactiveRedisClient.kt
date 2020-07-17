package com.test.tool.cache

import com.test.tool.lock.DistributeLock
import org.springframework.data.redis.core.StringRedisTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * @author 费世程
 * @date 2020/7/17 15:34
 */
class ReactiveRedisClient(private val template:StringRedisTemplate):ReactiveRedisCache,DistributeLock {
  override fun expire(key: String, expires: Duration): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun get(key: String): Mono<String?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> computeIfAbsent(key: String, clazz: Class<T>, timeout: Duration?, function: (String) -> T): T {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun multiGet(keys: Collection<String>): Mono<List<String>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun set(key: String?, value: String?, timeout: Duration?): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setAndExtendsTimeout(key: String, value: String): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAndSet(key: String, value: String): Mono<String?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setIfAbsent(key: String, value: String, timeout: Duration?): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setIfPresent(key: String, value: String, timeout: Duration?): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun incr(key: String, delta: Long): Mono<Long> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun decr(key: String, delta: Long): Mono<Long> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun delete(key: String?): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun delete(key: String, value: String): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hget(key: String, hashKey: String): Mono<String?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hgetAll(key: String): Flux<String> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hkeys(key: String): Flux<String> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hsize(key: String): Mono<Long> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun remove(key: String, vararg hashKeys: String): Mono<Long> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun entries(key: String): Flux<MutableMap.MutableEntry<String, String>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun put(key: String, hashKey: String, value: String): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun putAll(key: String, map: Map<String, String>): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun keys(pattern: String): Mono<MutableList<String>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun tryLock(lockKey: String, lockValue: String, expires: Duration): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun tryLock(lockKey: String, lockValue: String, expires: Duration, timeOut: Duration): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun unlock(lockKey: String, lockValue: String): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun forceUnLock(lockKey: String): Mono<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}