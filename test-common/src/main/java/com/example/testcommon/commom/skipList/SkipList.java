package com.example.testcommon.commom.skipList;

import java.util.LinkedList;
import java.util.Random;

/**
 * @anthor longzx
 * @create 2021 05 21 15:20
 * @Description 跳表抽象数据结构
 **/
public class SkipList {

    //使用头插法插入新节点
    private LinkedList<Node> head;//每一行的头结点，相当于跳表的第一列， 默认设置为 Integer.MIN_VALUE
    private LinkedList<Node> tail;//每一行大最后一个节点，相当与跳表的最后一列  Integer.MAX_VALUE
    private Random rd;//用于生成随机数数
    private int hight = -1;//当前跳表的层数，hight从0开始,初始值为-1，
    private int size;//所有的节点数

    public SkipList() {
        this.head = new LinkedList<>();
        this.tail = new LinkedList<>();
        this.rd = new Random();
    }

    public static void main(String[] args) {
        SkipList sl = new SkipList();
        int[] arr = new int[500];
        for (int i = 0; i < 500; i++) {
            arr[i] = (int) (Math.random() * 600);
        }
        sl.arrayToSkipList(arr);
        sl.showSkipList();
        System.out.println(sl.find(100));
        System.out.println(sl.find(50));
        System.out.println(sl.find(99));
        System.out.println("清空跳表");
        sl.clear();
        sl.showSkipList();

    }

    /**
     * 节点内部类
     */
    private class Node {
        int data;//存放数据
        Node next;//指向右边节点
        Node down; //指向下面节点
        int level;//当前所在的层

        public Node() {
        }

        public Node(int data, int level) {
            this.data = data;
            this.level = level;
        }

        public Node(int data, int level, Node next, Node down) {
            this.data = data;
            this.level = level;
            this.next = next;
            this.down = down;
        }
    }

    /**
     * 向跳表中加添加元素
     * 是否考虑重复元素？？？？？？？？？？
     *
     * @param val
     * @return
     */
    boolean add(int val) {
        int k = getLeavel();//获得层数
        //层数比当前大的时候，增加新的层
        if (k > hight) {
            int i = k - hight;
            for (int j = 1; j <= i; j++) {
                //新增头节点和尾节点
                Node h = new Node(Integer.MIN_VALUE, hight + j);
                if (head.size() > 0) {
                    h.down = head.getFirst();//往下指
                }
                Node t = new Node(Integer.MAX_VALUE, hight + j);//尾
                if (tail.size() > 0) {
                    t.down = tail.getFirst();
                }
                h.next = t;//头指向尾
                tail.addFirst(t);
                head.addFirst(h);
            }
            hight = k;//修改当前的跳表层数
        }
        return addFromK(val, k);//从第k层添加元素
    }

    /**
     * 从跳表的第k层新增元素
     * 被 add(int val) 方法调用
     */
    boolean addFromK(int val, int k) {
        Node preNewNode = new Node(val, k);
        Node preLine = head.get(hight - k);//获取新增节点所在层的头节点
        while (preLine != null) {
            while (preLine.next.data < val) {//往右搜索
                preLine = preLine.next;
            }
            preNewNode.next = preLine.next;
            preLine.next = preNewNode;
            //如果不是第一层，则建立下一层的新节点
            if (preNewNode.level > 0) {
                Node newNode = new Node(val, preNewNode.level - 1);
                preNewNode.down = newNode;//往下指向新节点
                preNewNode = newNode;
            }
            //往下层建立新节点
            preLine = preLine.down;
        }
        size++;//跳表中的元素数量加一
        return true;
    }


    /**
     * 随机获取高度,（相当于抛硬币连续出现正面的次数）
     *
     * @return
     */
    private int getLeavel() {
        int k = 0;
        while (rd.nextInt(2) == 1) {
            k++;
        }
        return k;
    }

    /**
     * 向跳表中删除元素，从上往下删除，每次找到所在行的前一个节点
     *
     * @param val
     * @return 如果找不到 待删除元素 则返回 false
     */
    boolean delete(int val) {
        Node lindPre = findPreNode(val);//找到待删除元素的最上层的前一个节点
        if (lindPre == null) {
            return false;
        }
        while (true) {
            lindPre.next = lindPre.next.next;
            lindPre = lindPre.down;//往下遍历，直到最底下一层
            if (lindPre == null) {
                break;
            }//跳出循环
            //找到待删除元素所在行的前一个节点
            while (lindPre.next.data != val) {
                lindPre = lindPre.next;
            }
        }
        size--;
        return true;
    }

    /**
     * 销毁跳表中的所有元素
     */
    void clear() {
        this.hight = -1;
        this.size = 0;
        this.head = null;
        this.tail = null;

    }

    /**
     * 查找跳表中是否存在该元素
     *
     * @param val
     * @return
     */
    boolean find(int val) {
        return findPreNode(val) != null;
    }

    /**
     * 找到元素 val 的前一个节点 即 最高层第一次出现的同一行的前一个元素
     */
    private Node findPreNode(int val) {
        Node first = head.getFirst();//从最上层的头节点开始搜索
        while (first != null) {
            if (first.data < val && first.next.data > val) {
                if (first.down == null) {
                    break;
                }
                first = first.down;//往下搜索
            } else if (first.data < val && first.next.data < val) {
                first = first.next;//往右搜索
            } else if (first.data < val && first.next.data == val) {
                return first;
            }
        }
        return null;
    }

    /**
     * 将数组中的元素添加到跳表中
     *
     * @param arr
     */
    void arrayToSkipList(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            add(arr[i]);
        }
    }

    /**
     * 从上到下打印跳表的内容
     */
    void showSkipList() {
        System.out.println("元素个数为：" + size);
        //从上往下逐层打印
        for (int i = 0; i <= hight; i++) {
            Node linFirst = head.get(i);
            System.out.print("第" + linFirst.level + "层:	" + "head ->	");
            linFirst = linFirst.next;//跳过第一列的元素
            while (linFirst != null) {
                if (linFirst.next != null) {
                    System.out.print("" + linFirst.data + '	' + "->	");//+ " height:"+linFirst.level
                } else {
                    System.out.println("tail");
                }
                linFirst = linFirst.next;
            }
            System.out.println();
        }
    }

}
