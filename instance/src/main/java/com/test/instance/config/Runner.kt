package com.test.instance.config

import com.test.tool.init.Destroy
import com.test.tool.init.Initialize
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/7/18 13:50
 */
@Component
class Runner(private val initialize: Initialize,
             private val destroy: Destroy) : ApplicationRunner {

  override fun run(args: ApplicationArguments?) {
    initialize.init()
    Runtime.getRuntime().addShutdownHook(Thread {
      destroy.toDestroy()
    })
  }

}