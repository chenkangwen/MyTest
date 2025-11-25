package com.example.testcommon.commom.collection;

import com.alibaba.fastjson.JSON;
import com.example.testapi.dto.UserDTO;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ListTest {

    private static List<UserDTO> stList = new ArrayList<>();

    static {
        stList.add(new UserDTO("路飞", 20));
        stList.add(new UserDTO("鸣人", 25));
        stList.add(new UserDTO("哪吒", new Date(1763913600000L), 10000));
        stList.add(new UserDTO("韩立", new Date(1763827200000L), 2000));
        stList.add(new UserDTO("古月方圆", new Date(1763740800000L), 600));
        stList.add(new UserDTO("萧炎", new Date(1763654400000L), 30));
        stList.add(new UserDTO("林动", new Date(1763568000000L), 40));
    }

    public static void main(String[] args) throws Exception {
        streamlistSort();
    }

    public void testRetainAll() {
        List<String> listOne = new ArrayList<>();
        listOne.add("aaa");
        listOne.add("bbb");
        listOne.add("ccc");
        listOne.add("ddd");
        listOne.add("eee");
        listOne.add("fff");

        List<String> listTwo = new ArrayList<>();
        listTwo.add("ccc");
        listTwo.add("ddd");
        listTwo.add("eee");
        listTwo.add("fff");

        listOne.retainAll(listTwo);

        System.out.println(JSON.toJSONString(listOne));
    }


    private static void testVector() {
        // 创建一个 Vector
        Vector<String> vector = new Vector<>();
        // 添加元素
        vector.add("hello");
    }

    private static void testStack() {
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
    }

    private static void testArrayDeque() {
        // 双端队列
        ArrayDeque<String> arrayDeque = new ArrayDeque();
        arrayDeque.add("hello");
    }


    private static void testLinkedList() {
        LinkedList<String> linkedList = new LinkedList<>();
        // 添加元素
        linkedList.add("hello");
    }


    private static void testCopyOnWriteArrayList() {
        // 创建一个 CopyOnWriteArrayList
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        // 添加元素
        copyOnWriteArrayList.add("hello");
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


    public static List<UserDTO> streamlistSort() {
        List<UserDTO> list = stList.stream().sorted(Comparator.comparing(UserDTO::getBirthDate, Comparator.nullsFirst(Date::compareTo)).reversed()).collect(Collectors.toList());
        for (UserDTO userDTO : list) {
            System.out.println(JSON.toJSONString(userDTO));
        }
        return list;
    }
}
