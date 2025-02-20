package com.example.testcommon.commom.algorithm.sorts;

/**
 * @description: 冒泡算法
 * @author: chenkangwen
 * @date: 2024/11/26
 */
public class BubbleSort {

    /**
     * @description: 循环数组，交换数组元素
     * @author: chenkangwen
     * @date: 2025/2/7
     * @param: [arr]
     */
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                swap(arr, j, j + 1);
            }
        }
    }

    /**
     * @description: 交换下标start, end的值;
     * @author: chenkangwen
     * @date: 2025/2/7
     * @param: [arr, start, end]
     */
    public static void swap(int[] arr, int start, int end) {
        if (arr[start] > arr[end]) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
    }
}
