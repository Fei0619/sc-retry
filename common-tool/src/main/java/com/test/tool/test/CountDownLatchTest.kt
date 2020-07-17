package com.test.tool.test

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

/**
 * 【CountDownLatch】
 * 是一个同步工具类，用来协调多个线程之间的同步，或者说起到线程之间的通信
 *
 * @author 费世程
 * @date 2020/7/16 16:15
 */
class CountDownLatch

/*
fun main() {
  val service = Executors.newFixedThreadPool(3)
  val latch = CountDownLatch(3)
  for (i in 0..3) {
    val runnable = {
      try {
        println("子线程" + Thread.currentThread().name + "开始执行")
        Thread.sleep((Math.random() * 10000).toLong())
        println("子线程" + Thread.currentThread().name + "执行完成")
        latch.countDown() //当前线程调用此方法，则计数减一
      } catch (e: InterruptedException) {
        e.printStackTrace()
      }
    }
    service.execute(runnable)
  }

  try {
    println("主线程" + Thread.currentThread().name + "等待子线程执行完成...")
    latch.await() //阻塞当前线程，直到计数器的值为0
    println("主线程" + Thread.currentThread().name + "开始执行...")
  } catch (e: InterruptedException) {
    e.printStackTrace()
  }
}
*/

/**
 * 百米赛跑，4名运动员选手到达场地等待裁判口令，裁判一声口令，选手听到后同时起跑，当所有选手到达终点，裁判进行汇总排名
 */
fun main() {
  val service = Executors.newCachedThreadPool()
  val cdOrder = CountDownLatch(1)
  val cdAnswer = CountDownLatch(4)
  for (i in 0..3) {
    val runnable = {
      try {
        println("选手【" + Thread.currentThread().name + "】正在等待裁判发布口令")
        cdOrder.await()
        println("选手【" + Thread.currentThread().name + "】已接受裁判口令")
        Thread.sleep((Math.random() * 10000).toLong())
        println("选手【" + Thread.currentThread().name + "】到达终点")
        cdAnswer.countDown()
      } catch (e: InterruptedException) {
        e.printStackTrace()
      }
    }
    service.execute(runnable)
  }
  try {
    Thread.sleep((Math.random() * 10000).toLong())
    println("裁判" + Thread.currentThread().name + "即将发布口令")
    cdOrder.countDown()
    println("裁判" + Thread.currentThread().name + "已发送口令，正在等待所有选手到达终点")
    cdAnswer.await()
    println("所有选手都到达终点")
    println("裁判" + Thread.currentThread().name + "汇总成绩排名")
  } catch (e: InterruptedException) {
    e.printStackTrace()
  }

  service.shutdown()
}