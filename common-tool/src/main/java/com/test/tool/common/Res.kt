package com.test.tool.common

/**
 * @author 费世程
 * @date 2020/7/20 19:00
 */
class Res<T> private constructor(code: Int, message: String, data: T, success: Boolean) {

  private var code: Int? = code
  private var message: String? = message
  private var data: T? = data
  private var success: Boolean? = success

  companion object {

    fun success(): Res<*> {
      return Res(200, "SUCCESS", null, true)
    }

    fun success(message: String): Res<*> {
      return Res(200, message, null, true)
    }

    fun <T> success(message: String, data: T): Res<T> {
      return Res(200, message, data, true)
    }

    fun err(): Res<*> {
      return Res(500, "FAILURE", null, true)
    }

    fun err(message: String): Res<*> {
      return Res(500, message, null, true)
    }

    fun <T> data(data: T): Res<T> {
      return Res(200, "SUCCESS", data, true)
    }

  }


}