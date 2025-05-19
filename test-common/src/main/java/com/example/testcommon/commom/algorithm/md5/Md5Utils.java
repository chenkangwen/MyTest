package com.example.testcommon.commom.algorithm.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: chenkangwen
 * @CreateTime: 2024-11-27  16:36
 * @Description:
 * @Version: 1.0
 */
public class Md5Utils {


    private static final String SHA1 = "SHA-1";

    private static final String MD5 = "MD5";


    public static void main(String[] args) {
        String s = encryptMD5("可口可乐,一瓶三块，三块一瓶");
        String s1 = encryptMD5("可口可乐,一瓶三块，三块一瓶");
        String s2 = encryptMD5("雪碧,一瓶三块，三块一瓶");
        System.out.println(s.equals(s1));
        System.out.println(s.equals(s2));
    }

    /**
     * @description: md5加密算法
     * @author: chenkangwen
     * @date: 2024/11/27
     * @param: [input]-----------
     */
    public static String encryptMD5(String input) {
        try {
            // 创建MD5加密对象
            MessageDigest md = MessageDigest.getInstance(MD5);
            // 执行加密操作
            byte[] messageDigest = md.digest(input.getBytes());
            // 将字节数组转换为16进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            // 返回加密后的字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
