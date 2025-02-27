package com.example.testcommon.commom.skipList;

import java.util.Random;
import java.util.Stack;

public class SkipList<T> {

    //头节点，入口
    private SkipNode headNode;

    //当前跳表索引层数
    private int highLevel;

    // 用于投掷硬币
    private Random random;

    //最大的层
    private final int MAX_LEVEL = 32;

    public SkipList() {

        random = new Random();

        // 头节点的key比较特殊，是int 最小值
        headNode = new SkipNode(Integer.MIN_VALUE, null);

        highLevel = 0;
    }

    public class SkipNode<T> {

        // 按照此字段进行排序
        private int key;

        private T value;

        //两个方向的指针
        private SkipNode right, down;

        public SkipNode(int key, T value) {
            this.key = key;
            this.value = value;
        }
    }


    /**
     * @description: 查询跳表
     * @author: chenkangwen
     * @date: 2025/2/27
     * @param: [key]
     */
    public SkipNode search(int key) {
        SkipNode team = headNode;
        while (team != null) {
            if (team.key == key) {
                return team;
            } else if (team.right == null) {
                team = team.down;
            } else if (team.right.key > key) {
                team = team.down;
            } else {
                team = team.right;
            }
        }
        return null;
    }

    /**
     * @description: 添加数据
     * @author: chenkangwen
     * @date: 2025/2/27
     * @param: [node]
     */
    public void add(SkipNode node) {
        int key = node.key;
        SkipNode findNode = search(key);
        if (findNode != null) {
            return;
        }
        Stack<SkipNode> stack = new Stack<SkipNode>();
        SkipNode team = headNode;
        while (team != null) {
            if (team.right == null) {
                stack.add(team);
                team = team.down;
            } else if (team.right.key > key) {
                stack.add(team);
                team = team.down;
            } else {
                team = team.right;
            }
        }

        int level = 1;
        SkipNode downNode = null;
        while (!stack.isEmpty()) {
            team = stack.pop();
            SkipNode nodeTeam = new SkipNode(node.key, node.value);
            nodeTeam.down = downNode;
            downNode = nodeTeam;
            if (team.right == null) {
                team.right = nodeTeam;
            } else {
                nodeTeam.right = team.right;
                team.right = nodeTeam;
            }
            if (level > MAX_LEVEL) {
                break;
            }
            double num = random.nextDouble();
            if (num > 0.5) {
                break;
            }
            level++;
            if (level > highLevel) {
                highLevel = level;
                SkipNode highHeadNode = new SkipNode(Integer.MIN_VALUE, null);
                highHeadNode.down = headNode;
                headNode = highHeadNode;
                stack.add(headNode);
            }
        }
    }


    /**
     * @description: 删除数据
     * @author: chenkangwen
     * @date: 2025/2/27
     * @param: [key]
     */
    public void delete(int key) {
        SkipNode team = headNode;
        while (team != null) {
            if (team.right == null) {
                team = team.down;
            } else if (team.right.key == key) {
                team.right = team.right.right;
                team = team.down;
            } else if (team.right.key > key) {
                team = team.down;
            } else {
                team = team.right;
            }
        }
    }
}