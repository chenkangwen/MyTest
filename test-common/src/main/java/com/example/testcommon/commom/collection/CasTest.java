package com.example.testcommon.commom.collection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-09-05  09:08
 * @Description:
 * @Version: 1.0
 */
public class CasTest {

    public static void main(String[] args) {
        // CAS操作
        AtomicInteger atomicInt = new AtomicInteger(10);
        atomicInt.compareAndSet(10, 1);
        atomicInt.incrementAndGet();
        System.out.println(atomicInt);



        String startTime = null;
        String endTime = null;
        Date startDate = null;
        Date endDate = null;

        Date date = new Date();

        // 当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 当前月
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        //-1得到上一个月
        calendar.add(Calendar.MONTH, -1);
        String lastMonth = format.format(calendar.getTime());
        startDate = calendar.getTime();
        //再-1得到上上一个月
        calendar.add(Calendar.MONTH, -1);
        String lastTwoMonth = format.format(calendar.getTime());
        if (currentMonth == 1) {
            startTime = lastTwoMonth + "-26" + " 00:00:00";
            endTime = lastMonth + "-30" + " 23:59:59";
        } else if (currentMonth == 2) {
            startTime = lastMonth + "-01" + " 00:00:00";
            endTime = lastMonth + "-25" + " 23:59:59";
        } else {
            startTime = lastTwoMonth + "-26" + " 00:00:00";
            endTime = lastMonth + "-25" + " 23:59:59";
        }

        String endExcelDate = new SimpleDateFormat("yyyy年MM月dd日").format(endDate);

        System.out.println(
                startTime + "  " + endTime
        );
    }
}
