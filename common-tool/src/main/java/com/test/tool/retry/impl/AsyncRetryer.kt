package com.test.tool.retry.impl

import com.test.tool.retry.AbstractRetryer
import java.util.concurrent.Future

/**
 * 异步重试器
 *
 * @author 费世程
 * @date 2020/7/16 20:00
 */
class AsyncRetryer<R> internal constructor() : AbstractRetryer<R, Future<R>>() {

}