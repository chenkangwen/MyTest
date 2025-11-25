package com.example.testcommon.commom.collection;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-05-09  10:21
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MathTest {

    public static void main(String[] args) {
        mathRound();
    }


    public static void mathRound() {
        long roundOne = Math.round(-3.5);
        long roundTwo = Math.round(3.5);
        log.info("roundOne：{}", roundOne);
        log.info("roundTwo：{}", roundTwo);
    }
}
