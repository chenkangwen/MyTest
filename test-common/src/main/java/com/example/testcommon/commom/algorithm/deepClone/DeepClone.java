package com.example.testcommon.commom.algorithm.deepClone;

import com.example.testcommon.commom.algorithm.innerClass.Food;

/**
 * @Author: chenkangwen
 * @CreateTime: 2026-03-05  11:18
 * @Description:
 * @Version: 1.0
 */
public class DeepClone {

    public static void main(String[] args) {
        Food food = new Food();
        food.setName("food的属性name");
        System.out.println(food.getName());
        deepClone(food);
        System.out.println(food.getName());
    }


    private static void deepClone(Food food) {
        food = new Food();
        food.setName("food的属性name新");
    }

}
