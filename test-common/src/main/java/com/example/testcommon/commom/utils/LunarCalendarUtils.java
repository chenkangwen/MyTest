package com.example.testcommon.commom.utils;

import com.xhinliang.lunarcalendar.LunarCalendar;

/**
 * @Author: chenkangwen
 * @CreateTime: 2026-03-26  15:12
 * @Description:
 * @Version: 1.0
 */
public class LunarCalendarUtils {
    public static void main(String[] args) {

        // 获取指定日期
        LunarCalendar date = LunarCalendar.obtainCalendar(2026, 4, 20);
        System.out.println("指定日期：" + date.getLunarYear() + "年" +
                date.getLunarMonth() + "月" + date.getLunarDay() + "日");

        // 整月日历（按周组织）
        LunarCalendar[][] month = LunarCalendar.obtainCalendar(2024, 2);
        for (LunarCalendar[] week : month) {
            for (LunarCalendar day : week) {
                System.out.print(day != null ? day.getLunarDay() + "\t" : " \t");
            }
            System.out.println();
        }


        // 获取节气名称（如"立春"、"雨水"等）
        String solarTerm = date.getSolarTerm();

        if (solarTerm != null) {
            System.out.println("今日节气：" + solarTerm);
        } else {
            System.out.println("今日无节气");

        }
    }
}
