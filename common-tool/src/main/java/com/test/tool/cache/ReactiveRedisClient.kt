package com.test.tool.cache

import com.test.tool.lock.DistributeLock
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * redisTemplate.opsForValue(); //操作字符串
 * redisTemplate.opsForHash();  //操作hash
 * redisTemplate.opsForList();  //操作list
 * redisTemplate.opsForSet();   //操作set
 * redisTemplate.opsForZSet();  //操作有序set
 *
 * @author 费世程
 * @date 2020/7/17 15:34
 */
class ReactiveRedisClient(private val template: ReactiveStringRedisTemplate) : ReactiveRedisCache, DistributeLock {

  /**
   * 将[key]的过期时间设为[expires]之后
   */
  override fun expire(key: String, expires: Duration): Mono<Boolean> {
    return template.expire(key, expires)
  }

  /**
   * 通过[key]获取value
   */
  override fun get(key: String): Mono<String?> {
    return template.opsForValue().get(key)
  }

  /**
   * 尝试从缓存中获取对象,如果缓存中不存在则调用[function]获取对象,并将结果存入缓存后返回获取的对象
   */
  override fun <T> computeIfAbsent(key: String, clazz: Class<T>, timeout: Duration?, function: (String) -> T): T {
    TODO()
  }

  /**
   * 通过[keys]批量获取value
   */
  override fun multiGet(keys: Collection<String>): Mono<List<String>> {
    TODO()
  }

  /**
   * 为[key]设置[value],并将[key]的超时时间设置为[timeout]
   * 默认情况下或者传入的[timeout]为空则不设置超时时间.
   * 如果[key]或[value]为空则返回false
   */
  override fun set(key: String?, value: String?, timeout: Duration?): Mono<Boolean> {
    if (key == null || value == null) return Mono.just(false)
    return if (timeout == null) {
      template.opsForValue().set(key, value)
    } else {
      template.opsForValue().set(key, value, timeout)
    }
  }

  /**
   * 为[key]设置[value]并继承原有的过期时间
   * 如果[key]为空或者没有过期时间,则会抛出异常
   */
  override fun setAndExtendsTimeout(key: String, value: String): Mono<Boolean> {
    return template.getExpire(key).flatMap {
      template.opsForValue().set(key, value, it)
    }
  }

  /**
   * 获取[key]对应的值,并将旧的值替换为[value]
   */
  override fun getAndSet(key: String, value: String): Mono<String?> {
    return template.opsForValue().getAndSet(key, value)
  }

  /**
   * 如果[key]**不存在**则设置[value],并将[key]的超时时间设置为[timeout]
   * 默认情况下或者传入的[timeout]为空则不设置超时时间
   */
  override fun setIfAbsent(key: String, value: String, timeout: Duration?): Mono<Boolean> {
    return if (timeout == null) {
      template.opsForValue().setIfAbsent(key, value)
    } else {
      template.opsForValue().setIfAbsent(key, value, timeout)
    }
  }

  /**
   * 如果[key]**存在**则设置[value],并将[key]的超时时间设置为[timeout]
   * 默认情况下或者传入的[timeout]为空则不设置超时时间
   */
  override fun setIfPresent(key: String, value: String, timeout: Duration?): Mono<Boolean> {
    return if (timeout == null) {
      template.opsForValue().setIfPresent(key, value)
    } else {
      template.opsForValue().setIfPresent(key, value, timeout)
    }
  }

  /**
   * 自增
   */
  override fun incr(key: String, delta: Long): Mono<Long> {
    return template.opsForValue().increment(key, delta)
  }

  /**
   * 自减
   */
  override fun decr(key: String, delta: Long): Mono<Long> {
    return template.opsForValue().decrement(key, delta)
  }

  /**
   * 删除
   */
  override fun delete(key: String?): Mono<Boolean> {
    if (key == null) return Mono.just(true)
    return template.opsForValue().delete(key)
  }

  /**
   * 当[key]和[value]同时满足时删除[key]
   */
  override fun delete(key: String, value: String): Mono<Boolean> {
    TODO()
  }

  /**
   * 从[key]的hash结构中获取给定[hashKey]的值
   */
  override fun hget(key: String, hashKey: String): Mono<String?> {
    TODO()
  }

  override fun hgetAll(key: String): Flux<String> {
    TODO()
  }

  override fun hkeys(key: String): Flux<String> {
    TODO()
  }

  override fun hsize(key: String): Mono<Long> {
    TODO()
  }

  override fun remove(key: String, vararg hashKeys: String): Mono<Long> {
    TODO()
  }

  override fun entries(key: String): Flux<MutableMap.MutableEntry<String, String>> {
    TODO()
  }

  override fun put(key: String, hashKey: String, value: String): Mono<Boolean> {
    TODO()
  }

  override fun putAll(key: String, map: Map<String, String>): Mono<Boolean> {
    TODO()
  }

  override fun keys(pattern: String): Mono<MutableList<String>> {
    TODO()
  }

  override fun tryLock(lockKey: String, lockValue: String, expires: Duration): Mono<Boolean> {
    return set(lockKey, lockValue, expires)
  }

  override fun tryLock(lockKey: String, lockValue: String, expires: Duration, timeOut: Duration): Mono<Boolean> {
    TODO()
  }

  override fun unlock(lockKey: String, lockValue: String): Mono<Boolean> {
    return delete(lockKey, lockValue)
  }

  override fun forceUnLock(lockKey: String): Mono<Boolean> {
    return delete(lockKey)
  }
}