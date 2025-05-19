package com.example.testcommon.commom.collection;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-09-05  09:08
 * @Description:
 * @Version: 1.0
 */
public class CasTest {

    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<Integer>
            (100,0);

    public static void main(String[] args) {

        AtomicInteger atomicInt = new AtomicInteger(10);
        atomicInt.compareAndSet(10, 1); // CAS 操作
        atomicInt.incrementAndGet();
        System.out.println(atomicInt);




    }
}
