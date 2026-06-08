package com.example.testcommon.commom.collection;

import java.math.BigDecimal;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-05-09  16:47
 * @Description:
 * @Version: 1.0
 */
public class StringTest {

    public static void main(String[] args) {
        String str = "";
        BigDecimal bd = new BigDecimal("0.0000");
        // 通过返回值获取修改后的值（String 和 BigDecimal 是不可变对象，Java 是值传递）
        String newStr = testStr(str);
        BigDecimal newBd = testBd(bd);
        System.out.println("str:" + newStr);
        System.out.println("bd:" + newBd);
    }


    public static void reverseString(char[] s) {
        int l = 0;
        int r = s.length - 1;
        while (l < r) {
            char temp = s[l];
            s[l] = s[r];
            s[r] = temp;
            l++;
            r--;
        }
    }

    public static String testStr(String str) {
        return "hello world";
    }

    public static BigDecimal testBd(BigDecimal bd) {
        return new BigDecimal("123.456");
    }


}
