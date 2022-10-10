package com.example;

import com.example.util.ConcurrentUtil;

public class Main {
    public static void main(String[] args) {
        new Thread(()->{
            System.out.println("1");
            ConcurrentUtil.waitIt();
            System.out.println("4");
        }).start();
        new Thread(()->{
            System.out.println("2");
            ConcurrentUtil.continueIt();
            System.out.println("3");
        }).start();
    }
}
