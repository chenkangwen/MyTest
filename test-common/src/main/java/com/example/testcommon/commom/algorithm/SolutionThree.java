package com.example.testcommon.commom.algorithm;

public class SolutionThree {


    public static void main(String[] args) {
        int[] arr = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println(solution(arr));
    }

    public static int solution(int[] arr) {
        int l = 0;
        int r = arr.length - 1;
        int ans = 0;
        while (l < r) {
            int temp = Math.min(arr[l], arr[r]) * (r - l);
            ans = Math.max(ans, temp);
            if (arr[r] >= arr[l]) {
                l++;
            } else {
                r--;
            }
        }
        return ans;
    }

}
