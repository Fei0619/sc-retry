package com.test.tool.test

import java.util.concurrent.locks.ReentrantLock

/**
 * ReentrantLock是一个互斥锁，也是一个可重入锁
 * Lock是接口，ReentrantLock是Lock的具体实现
 * Lock和synchronized的区别：
 *  1.synchronized可以给类、方法、代码块加锁，Lock只能给代码块加锁
 *  2.synchronized不需要手动的获取锁和释放锁，发生异常时会自动释放锁不会造成死锁，
 *    而Lock需要自己加锁和释放锁，使用不当可能会造成死锁
 *  3.Lock可以知道有没有成功的获取锁，而synchronized不能
 *
 * @author 费世程
 * @date 2020/7/15 19:56
 */
class ReentrantLockTest

class Counter {

  companion object {
    private var count = 100
    /**
     * ReentrantLock()             非公平锁
     * ReentrantLock(boolean fair) 公平锁
     */
    private val lock = ReentrantLock(true)

    /*
    由于上锁之后后面的代码可能会因为抛出异常而没能释放锁，所以，锁的释放一定要在try-finally块的finally中
     */
    fun desc() {
      try {
        lock.lock()   //上锁
        System.err.println("${Thread.currentThread().name} ---> $count")
        count--
      } finally {
        lock.unlock() //释放锁
      }
    }

    fun getCount(): Int {
      return count
    }

  }
}

fun main() {

  val runnable = Runnable {
    while (Counter.getCount() > 0) {
      Counter.desc()
    }
  }

  Thread(runnable).start()
  Thread(runnable).start()

}