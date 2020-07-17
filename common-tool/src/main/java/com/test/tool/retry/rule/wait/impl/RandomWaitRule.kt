package com.test.tool.retry.rule.wait.impl

import com.test.tool.retry.rule.wait.WaitRule
import java.util.concurrent.ThreadLocalRandom

/**
 * 随机等待时长策略
 * @author 费世程
 * @date 2020/7/16 19:28
 */
class RandomWaitRule(private val upperMillis: Long, private val lowerMillis: Long) : WaitRule {

  init {
    require(upperMillis > 0) { "随机等待时间上限应大于0ms" }
    require(lowerMillis > 0) { "随机等待时间下限应大于0ms" }
  }

  override fun delayMillis(): Long {
    /*
    ThreadLocalRandom的实例instance是static修饰的，所以对于多个线程，实例其实只有一个

    JDK8中ThreadLocalRandom的实现方法发生了变化，它不再使用ThreadLocalRandom实例本身维护线程各自的种子变量，而是将种子变量转移到Thread类中
    ThreadLocalRandom使用ThreadLocal的原理，让每个线程内持有一个本地的种子变量，该种子变量只有在使用随机数时候才会被初始化，
    多线程下计算新种子时候是根据自己线程内维护的种子变量进行更新，从而避免了竞争
    由于具体的种子是存放到线程里面的，所以ThreadLocalRandom的实例里面只是与线程无关的通用算法，所以是线程安全的

    一定不要在多线程之间共享同一个ThreadLocalRandom实例，这样会导致多线程产生相同的随机数
    多线程情况下，ThreadLocalRandom.current()方法会帮我们找到各自线程内部维护的种子，而产生各自的随机数
     */
    return ThreadLocalRandom.current().nextLong(upperMillis, lowerMillis)
  }

}