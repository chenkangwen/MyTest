package com.example.testcommon.commom.collection;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-09-05  09:08
 * @Description:
 * @Version: 1.0
 */
public class CasTest {

    public static void main(String[] args) {
        // CAS操作
        AtomicInteger atomicInt = new AtomicInteger(10);
        atomicInt.compareAndSet(10, 1);
        atomicInt.incrementAndGet();
        System.out.println(atomicInt);
    }
}
