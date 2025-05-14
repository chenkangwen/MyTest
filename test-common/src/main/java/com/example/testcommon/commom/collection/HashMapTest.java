package com.example.testcommon.commom.collection;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenkangwen
 * @CreateTime: 2023-05-30  17:13
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class HashMapTest {


    public static void main(String[] args) {
        mapRemove();
    }


    /**
     * @description: 测试map的去除
     * @author: chenkangwen
     * @date: 2025/5/9
     * @param: []
     */
    public static void mapRemove() {
        List<String> list = Arrays.asList("xixi", "hehe", "haha", "huhu", "caodan");
        Map<String, String> map = new HashMap<>();
        map.put("xixi", "xixi");
        map.put("hehe", "hehe");
        map.put("enen", "enen");
        for (String key : list) {
            String remove = map.remove(key);
            log.info("key:{},value:{}", key, remove);
        }
        log.info("map:{}", JSON.toJSONString(map));
    }


    public static void testHashTable() {
        Hashtable<Object, Object> hashtable = new Hashtable<>();

    }


}
