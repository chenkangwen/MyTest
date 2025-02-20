package com.example.testcommon.commom.algorithm.sorts;

/**
 * @description: 快速排序
 * @author: chenkangwen
 * @date: 2025/2/7
 * @param:
 */
public class SelectionSort {

    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] > arr[minIndex]) {
                    minIndex = j;
                }
            }
            // 交换arr[i]和arr[minIndex]
            swap(arr, minIndex, i);
        }
    }


    /**
     * @description: 交换下标start, end的值;
     * @author: chenkangwen
     * @date: 2025/2/7
     * @param: [arr, start, end]
     */
    public static void swap(int[] arr, int start, int end) {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
    }
}
