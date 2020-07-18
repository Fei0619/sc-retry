package com.test.tool.retry.impl

import com.test.tool.retry.AbstractRetryer
import reactor.core.publisher.Mono

/**
 * 反应式重试器
 *
 * @author 费世程
 * @date 2020/7/17 16:22
 */
class ReactiveRetryer<R>() : AbstractRetryer<R, Mono<R>>() {
  override fun execute(): Mono<R> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}