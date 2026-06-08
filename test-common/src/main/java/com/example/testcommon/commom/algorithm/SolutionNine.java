package com.example.testcommon.commom.algorithm;

import java.util.Arrays;

public class SolutionNine {


    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int best = 10000000;
        for (int i = 0; i < n; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = i + 1, k = n - 1;
            while (j < k) {

            }


        }
        return best;
    }


}
