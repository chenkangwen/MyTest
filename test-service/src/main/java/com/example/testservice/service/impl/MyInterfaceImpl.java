package com.example.testservice.service.impl;

import com.example.testapi.service.MyInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-08-05  09:52
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MyInterfaceImpl implements MyInterface {

    @Override
    public void doSomething() {
        log.info("-----doSomething-----");
    }

    @Override
    public void notDoSomething() {
        log.info("-----notDoSomething-----");
    }
}
