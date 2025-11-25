package com.example.testcommon.commom.collection;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-05-09  16:47
 * @Description:
 * @Version: 1.0
 */
public class StringTest {

    public static void main(String[] args) {
        String str = "dlrow olleh";
        char[] chars = str.toCharArray();
        reverseString(chars);
        System.out.println(new String(chars));
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

    public static void test() {
        String str = "hello world";
        char[] chars = str.toCharArray();
        System.out.println(new String(chars));
    }


}
