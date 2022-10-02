package com.example.demo.commom.collection;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.UserDTO;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ListTest {

    public static void main(String[] args) {
        LinkedList<String> llt = new LinkedList<>();
        llt.add("hello");

        Vector<String> vector = new Vector<>();
        vector.add("hello");
    }

    public static void test() {
        List<UserDTO> list = new ArrayList<>();
        list.add(new UserDTO("西施", 18));
        list.add(new UserDTO("王昭君", 16));
        list.add(new UserDTO("杨昭君", 20));
        list.add(new UserDTO("貂蝉", 24));
        list.add(new UserDTO("猪八戒", 5));
        Collections.sort(list, new Comparator<UserDTO>() {
            @Override
            public int compare(UserDTO o1, UserDTO o2) {
                //第一个参数属性 - 第二个参数属性 = 升序排序
                //第二个参数属性 - 第一个参数属性 = 降序排序
                int num = o2.getAge() - o1.getAge();
                if (num > 0) {
                    return 1;
                } else if (num < 0) {
                    return -1;
                }
                return 0;
            }
        });
        System.err.println(JSON.toJSONString(list));
    }

    public static void test_1() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("hahaha");
        linkedList.add("hehehe");
        for (String str :linkedList){

        }
        String first = linkedList.getFirst();
        System.out.println(first);
    }

}
