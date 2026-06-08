package com.example.testcommon.commom.algorithm;

public class SolutionFour {

    private static int[] values = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    private static String[] romans = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};


    public static void main(String[] args) {
        System.out.println(intToReman(2345));
    }


    private static String intToReman(int target) {
        StringBuffer ans = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            int value = values[i];
            while (target >= value) {
                target -= value;
                ans.append(romans[i]);
            }
            if (target == 0) {
                break;
            }
        }
        return ans.toString();
    }


}
