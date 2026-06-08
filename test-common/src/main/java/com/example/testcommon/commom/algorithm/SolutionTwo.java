package com.example.testcommon.commom.algorithm;

import java.util.HashMap;
import java.util.Map;

public class SolutionTwo {

    public static int sign = 1;

    public static long anx = 0;

    private static String state = "start";


    public static void main(String[] args) {
        System.out.println(myAtoi("  42"));
        System.out.println(myAtoi("  -589"));
        System.out.println(myAtoi("  568"));
    }


    public static int myAtoi(String str) {
        sign = 1;
        anx = 0l;
        state = "start";
        int length = str.length();
        for (int i = 0; i < length; ++i) {
            get(str.charAt(i));
        }
        return (int) (sign * anx);
    }


    private static Map<String, String[]> table = new HashMap<String, String[]>() {{
        put("start", new String[]{"start", "signed", "in_number", "end"});
        put("signed", new String[]{"end", "end", "in_number", "end"});
        put("in_number", new String[]{"end", "end", "in_number", "end"});
        put("end", new String[]{"end", "end", "end", "end"});
    }};

    public static void get(char c) {
        state = table.get(state)[get_col(c)];
        if ("in_number".equals(state)) {
            anx = anx * 10 + c - '0';
            anx = sign == 1 ? Math.min(anx, (long) Integer.MAX_VALUE) : Math.min(anx, -(long) Integer.MIN_VALUE);
        } else if ("signed".equals(state)) {
            sign = c == '+' ? 1 : -1;
        }
    }


    private static int get_col(char c) {
        if (c == ' ') {
            return 0;
        }
        if (c == '+' || c == '-') {
            return 1;
        }
        if (Character.isDigit(c)) {
            return 2;
        }
        return 3;
    }


}
