package com.example.testservice.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: chenkangwen
 * @CreateTime: 2024-11-26  14:31
 * @Description:
 * @Version: 1.0
 */
public class DateUtils {

    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);


    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date parseDate(String dateStr) {
        Date date = null;
        try {
            date = simpleDateFormat.parse("2024-07-29 19:34:41");
        } catch (ParseException e) {
            logger.error("时间字符串解析报错,e:{}", e);
        }
        return date;
    }


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 40);
        System.out.println(JSON.toJSONString(simpleDateFormat.format(calendar.getTime())));
    }

}
