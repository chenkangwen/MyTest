package com.example.testcommon.commom.collection;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: chenkangwen
 * @CreateTime: 2023-05-30  17:13
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MapTest {


    //
    public static void main(String[] args) {
        // Throwable throwable = new Throwable();
        // 页面认领部门字段拼接
        StringBuilder officeNameStr = new StringBuilder();
        if (Objects.nonNull(officeNameStr)) {
            officeNameStr.append("---------------------------------");
            System.out.println(officeNameStr.toString());
        }
    }


    public static void hashMapTest() {
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

    public static void treeMapTest() {

        TreeMap<String, String> treemap2 = new TreeMap<>();
        treemap2.put("z", "five");
        treemap2.put("s", "six");
        treemap2.put("a", "one");
        treemap2.put("b", "two");
        treemap2.put("c", "three");

        for (Map.Entry<String, String> entry : treemap2.entrySet()) {
            log.info("遍历treeMap：{}", entry.getKey());
        }


        TreeMap<Integer, String> treemap = new TreeMap<>();
        treemap.put(6, "six");
        treemap.put(5, "five");
        treemap.put(3, "three");
        treemap.put(2, "two");
        treemap.put(1, "one");

        SortedMap<Integer, String> treemapincl;
        treemapincl = treemap.tailMap(3);
        log.info("Tail map values:{}", treemapincl);
        treemapincl = treemap.headMap(3);
        log.info("Head map values:{} ", treemapincl);
        log.info("First key is: {}", treemap.firstKey());
        log.info("Last key is:{} ", treemap.lastKey());
    }

    public static void linkedHashMapTest() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("one", "one");


    }


    public static void hashTableTest() {
        Hashtable<Object, Object> hashtable = new Hashtable<>();
    }


}
