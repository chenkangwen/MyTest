package com.example.testcommon.commom.newFeatures;


import java.util.ArrayList;
import java.util.List;

public class NewFeatures {


    public static void main(String[] args) {
        //接口定义默认方法
        //NewCharacter newCharacter = new NewCharacterImpl();
        //newCharacter.test();

        // MyLamda m = y -> System.out.println(1000+y);
        // m.test1("1111");

        List<String> labelList = new ArrayList<>();
        labelList.add("哈哈哈");

        String join = String.join(",", labelList);

        System.out.println(join);

    }
}
