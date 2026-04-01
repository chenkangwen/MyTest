package com.example.testcommon.commom.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        //当前年份
        int year = today.getYear();
        // 获取当前月份
        int monthValue = today.getMonthValue();
        //当前年日
        int dayOfYear = today.getDayOfYear();
        //当前月日
        int dayOfMonth = today.getDayOfMonth();
        System.out.println("当前年份：" + year + "----" + "当前月份：" + monthValue + "----" + "当前日是: " + dayOfMonth);

        String formatted = String.format("%02d", 15);
        System.out.println("当前月份：" + formatted);
    }

    public static String formatDateToStr(Date date) throws ParseException {
        synchronized (formatYMD) {
            return formatYMD.format(date);
        }
    }


    public static Date parseStrToDate(String strDate) throws ParseException {
        synchronized (formatYMD) {
            return formatYMD.parse(strDate);
        }
    }

    /**
     * @return
     * @Description 根据指定日期, 增加或者减少天数
     * @Author wuxin
     * @Param
     * @Version 2019/10/17 18:07
     */
    public static Date addDay(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return calendar.getTime();
    }

}
