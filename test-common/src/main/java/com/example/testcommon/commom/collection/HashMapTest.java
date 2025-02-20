package com.example.testcommon.commom.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: chenkangwen
 * @CreateTime: 2023-05-30  17:13
 * @Description:
 * @Version: 1.0
 */
public class HashMapTest {


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("hehe","hehe");
        String hehe = map.get("hehe");
        System.out.println(hehe);

    }

}
