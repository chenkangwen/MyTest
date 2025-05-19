package com.example.testcommon.commom.lockTest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    private Lock lock = new ReentrantLock();

    private static volatile Integer tagert = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                tagert++;
            }).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                tagert--;
            }).start();
        }
        System.out.println(tagert);
    }
}
