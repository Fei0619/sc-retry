package com.test.tool.retry.impl

import com.test.tool.retry.AbstractRetryer
import java.util.*

/**
 * 阻塞重试器
 *
 * @author 费世程
 * @date 2020/7/17 16:21
 */
class BlockRetryer<R>() : AbstractRetryer<R, Optional<R>>() {
  override fun execute(): Optional<R> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}