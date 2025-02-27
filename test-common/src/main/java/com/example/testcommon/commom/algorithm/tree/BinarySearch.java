package com.example.testcommon.commom.algorithm.tree;

public class BinarySearch {

    // 二分查找算法实现
    public static int binarySearch(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            // 检查中间元素是否是目标值
            if (array[mid] == target) {

                return mid;
            }

            // 如果目标值大于中间元素，则忽略左半部分
            if (array[mid] < target) {

                left = mid + 1;

            } else { // 如果目标值小于中间元素，则忽略右半部分

                right = mid - 1;

            }
        }
        // 如果未找到目标值，则返回 -1
        return -1;
    }

    public static void main(String[] args) {
        // 示例有序数组
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        // 要查找的目标值
        int target = 5;

        // 调用二分查找方法
        int result = binarySearch(array, target);

        // 输出结果
        if (result != -1) {
            System.out.println("目标值 " + target + " 在数组中的索引位置为: " + result);
        } else {
            System.out.println("目标值 " + target + " 不在数组中");
        }
    }
}
