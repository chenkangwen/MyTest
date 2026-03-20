package com.example.testapi.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: chenkangwen
 * @CreateTime: 2026-03-05  11:21
 * @Description: 深拷贝测试类
 * @Version: 1.0
 */
@Data
public class Person implements Cloneable, Serializable {

    private String name;

    private Integer age;

    private Money money = new Money();


    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person person = (Person) super.clone();
        person.money = (Money) this.money.clone();
        return person;
    }
}
