package com.example.testcommon.commom.collection;

import com.alibaba.fastjson.JSON;
import com.example.testapi.dto.UserDTO;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ListTest {

    private static List<UserDTO> list = new ArrayList<>();

    static {
        list.add(new UserDTO("路飞", 50));
        list.add(new UserDTO("鸣人", 18));
        list.add(new UserDTO("哪吒", 16));
        list.add(new UserDTO("韩立", 20));
        list.add(new UserDTO("古月方圆", 80));
        list.add(new UserDTO("萧炎", 5));
        list.add(new UserDTO("林动", null));
    }



    public static void main(String[] args) {
        LinkedList<String> llt = new LinkedList<>();
        llt.add("hello");

        // 创建一个 Vector
        Vector<String> vector = new Vector<>();
        // 添加元素
        vector.add("aaa");

        Stack<String> stack = new Stack<>();
        // 添加元素
        stack.push("aaa");
        stack.push("bbb");
        stack.push("ccc");
        stack.push("ddd");
        stack.push("eee");
        stack.push("fff");

        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.peek());

        // 创建一个 CopyOnWriteArrayList
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        // 添加元素
        copyOnWriteArrayList.add("aaa");

        ArrayDeque arrayDeque = new ArrayDeque();

        listSort(list);
        for (UserDTO userDTO : list) {
            System.out.println(JSON.toJSONString(userDTO));
        }
        streamlistSort(list);
    }

    public static List<UserDTO> listSort(List<UserDTO> list) {
        Collections.sort(list, (o1, o2) -> {
            //第一个参数属性 - 第二个参数属性 = 升序排序
            //第二个参数属性 - 第一个参数属性 = 降序排序
            if (Objects.isNull(o1.getAge()) || Objects.isNull(o2.getAge())) {
                return 1;
            }
            return o1.getAge() - o2.getAge();
        });
        return list;
    }


    public static List<UserDTO> streamlistSort(List<UserDTO> list) {
        list = list.stream().sorted(Comparator.comparing(UserDTO::getAge, Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList());
        return list;
    }
}
