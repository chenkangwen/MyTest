package com.example.testcommon.commom.algorithm.innerClass;

import lombok.Data;

@Data
public class Food {
    private String name = "food的私有属性";

    public String getName() {
        return name;
    }
}