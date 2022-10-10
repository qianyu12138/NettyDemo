package com.example.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentUtil {

    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void waitIt(){
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

    public static void continueIt(){
        lock.lock();
        condition.signal();
        lock.unlock();
    }

}
