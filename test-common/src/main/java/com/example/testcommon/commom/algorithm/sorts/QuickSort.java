package com.example.testcommon.commom.algorithm.sorts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickSort {

    /**
     * 日志对象
     */
    public static Logger logger = LoggerFactory.getLogger(QuickSort.class);

    /**
     * @description: 快速排序
     * @author: chenkangwen
     * @date: 2024/11/27
     * @param: [arr, left, right]
     */
    public static void quickSort(int[] arr, int left, int right) {
        if (left > right) {
            return;
        }
        int i = left;
        int j = right;
        int temp = arr[left];
        while (i < j) {
            //先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            //如果满足条件则交换
            if (i < j) {
                swap(arr, i, j);
            }
        }
        //最后将基准为与i和j相等位置的数字交换
        arr[left] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, left, j - 1);
        //递归调用右半数组
        quickSort(arr, j + 1, right);
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
