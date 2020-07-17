package com.test

/**
 * @author 费世程
 * @date 2020/7/16 14:45
 */
fun main() {
  val t1 = Thread {
    Thread.sleep(1000)
    System.err.println("线程1执行完成..")
  }

  t1.start()
  /*
    Thread中,join()方法的作用是调用线程等待该线程完成后，才能继续用下运行
   */
//  t1.join()
  System.err.println("main执行完毕")
}