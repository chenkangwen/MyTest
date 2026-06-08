package com.example.testcommon.commom.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtractor {

    /**
     * 提取 "改为" 后面的第一个双引号内的字符串。
     * 示例: "[看楼书编号]:\"GZJJZ1201304\"改为\"GZJJZ6250002\"" -> "GZJJZ6250002"
     * 若未找到，返回 null。
     */
    public static String extractAfterChanged(String text) {
        if (text == null) {
            return null;
        }
        // 模式：改为"（中间任意非引号字符）"
        Pattern pattern = Pattern.compile("改为\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 提取文本中所有双引号内的字符串（通用方法）。
     * 返回字符串列表，若无则返回空列表。
     */
    public static List<String> extractAllQuoted(String text) {
        List<String> result = new ArrayList<>();
        if (text == null) {
            return result;
        }
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    public static void main(String[] args) {
        String test = "[看楼书编号]:\"GZJJZ1201326\"改为\"GZJJZ1201323\"";

        // 测试提取改为后的内容
        String changed = extractAfterChanged(test);
        System.out.println("改为后的新编号: " + changed);  // 输出: GZJJZ6250002

        // 测试提取所有双引号内容
        List<String> allQuoted = extractAllQuoted(test);
        System.out.println("所有双引号内容: " + allQuoted); // 输出: [GZJJZ1201304, GZJJZ6250002]
    }
}