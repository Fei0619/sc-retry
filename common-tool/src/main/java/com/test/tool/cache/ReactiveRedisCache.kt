package com.test.tool.cache

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * @author 费世程
 * @date 2020/7/17 15:32
 */
interface ReactiveRedisCache {
  /**
   * 将[key]的过期时间设为[expires]之后
   */
  fun expire(key: String, expires: Duration): Mono<Boolean>

  /**
   * 通过[key]获取value
   */
  fun get(key: String): Mono<String?>

  /**
   * 尝试从缓存中获取对象,如果缓存中不存在则调用[function]获取对象,并将结果存入缓存后返回获取的对象
   */
  fun <T> computeIfAbsent(key: String, clazz: Class<T>, timeout: Duration? = null, function: (String) -> T): T

  /**
   * 通过[keys]批量获取value
   */
  fun multiGet(keys: Collection<String>): Mono<List<String>>

  /**
   * 为[key]设置[value],并将[key]的超时时间设置为[timeout]
   * 默认情况下或者传入的[timeout]为空则不设置超时时间.
   * 如果[key]或[value]为空则返回false
   */
  fun set(key: String?, value: String?, timeout: Duration? = null): Mono<Boolean>

  /**
   * 为[key]设置[value]并继承原有的过期时间
   * 如果[key]为空或者没有过期时间,则会抛出异常
   */
  fun setAndExtendsTimeout(key: String, value: String): Mono<Boolean>

  /**
   * 获取[key]对应的值,并将旧的值替换为[value]
   */
  fun getAndSet(key: String, value: String): Mono<String?>

  /**
   * 如果[key]**不存在**则设置[value],并将[key]的超时时间设置为[timeout]
   * 默认情况下或者传入的[timeout]为空则不设置超时时间
   */
  fun setIfAbsent(key: String, value: String, timeout: Duration? = null): Mono<Boolean>

  /**
   * 如果[key]**存在**则设置[value],并将[key]的超时时间设置为[timeout]
   * 默认情况下或者传入的[timeout]为空则不设置超时时间
   */
  fun setIfPresent(key: String, value: String, timeout: Duration? = null): Mono<Boolean>

  /**
   * 自增
   */
  fun incr(key: String, delta: Long = 1L): Mono<Long>

  /**
   * 自减
   */
  fun decr(key: String, delta: Long = 1L): Mono<Long>

  /**
   * 删除
   */
  fun delete(key: String?): Mono<Boolean>

  /**
   * 当[key]和[value]同时满足时删除[key]
   */
  fun delete(key: String, value: String): Mono<Boolean>

  /**
   * 从[key]的hash结构中获取给定[hashKey]的值
   */
  fun hget(key: String, hashKey: String): Mono<String?>

  /**
   * 从[key]的hash结构中获取所有值
   */
  fun hgetAll(key: String): Flux<String>

  /**
   * 从[key]的hash结构中获取所有hashKey
   */
  fun hkeys(key: String): Flux<String>

  /**
   * 获取[key]的hash结构大小
   */
  fun hsize(key: String): Mono<Long>

  fun remove(key: String, vararg hashKeys: String): Mono<Long>

  fun entries(key: String): Flux<MutableMap.MutableEntry<String, String>>

  fun put(key: String, hashKey: String, value: String): Mono<Boolean>

  fun putAll(key: String, map: Map<String, String>): Mono<Boolean>

  fun keys(pattern: String): Mono<MutableList<String>>
}