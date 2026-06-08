package com.example.testcommon.commom.algorithm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class ParkingFeeCalculator {

    // 常量配置
    private static final int HOURLY_RATE = 2;          // 每小时2元
    private static final int CAP_WEEKDAY = 25;         // 工作日封顶
    private static final int CAP_WEEKEND = 30;         // 周末封顶
    private static final int DAY_START_HOUR = 4;       // 计费日起始小时

    /**
     * 计算停车费
     * @param start 入场时间
     * @param end   出场时间（必须晚于 start）
     * @return      总费用（元）
     */
    public static double calculate(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end) || start.equals(end)) {
            return 0.0;
        }

        double totalFee = 0.0;
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            // 1. 获取当前时刻所在的计费日起始时刻（凌晨4点）
            LocalDateTime dayStart = getDayStart(current);
            LocalDateTime dayEnd = dayStart.plusDays(1); // 次日凌晨4点

            // 2. 当前片段不能越过该计费日的结束边界
            LocalDateTime segmentEnd = end.isBefore(dayEnd) ? end : dayEnd;

            // 3. 计算该片段的秒数，并转为小时（向上取整）
            long seconds = Duration.between(current, segmentEnd).getSeconds();
            int hours = (int) Math.ceil(seconds / 3600.0);

            // 4. 按小时费用和封顶价
            double hourlyCost = hours * HOURLY_RATE;
            int cap = getCap(dayStart);  // 根据计费日起始日判定

            // 5. 本片段实际费用
            double segmentFee = Math.min(hourlyCost, cap);
            totalFee += segmentFee;

            // 6. 移动到下一片段
            current = segmentEnd;
        }

        return totalFee;
    }

    /**
     * 返回给定时刻所属计费日的起始时刻（当天凌晨4点，若时间<4点则为前一天凌晨4点）
     */
    private static LocalDateTime getDayStart(LocalDateTime dateTime) {
        LocalDateTime today4am = dateTime.with(LocalTime.of(DAY_START_HOUR, 0, 0));
        if (dateTime.isBefore(today4am)) {
            // 时间在4点之前，属于前一天的计费日
            return today4am.minusDays(1);
        } else {
            return today4am;
        }
    }

    /**
     * 根据计费日的起始日返回封顶价
     */
    private static int getCap(LocalDateTime dayStart) {
        DayOfWeek dow = dayStart.getDayOfWeek();
        // 周六(SATURDAY) 或 周日(SUNDAY) 为周末
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return CAP_WEEKEND;
        } else {
            return CAP_WEEKDAY;
        }
    }

    // ------------------- 测试入口 -------------------
    public static void main(String[] args) {
        // 测试用例1：周五 00:00 ~ 周五 03:00 （同一天计费日内，3小时）
        LocalDateTime t1Start = LocalDateTime.of(2026, 7, 17, 0, 0);  // 周五
        LocalDateTime t1End   = LocalDateTime.of(2026, 7, 17, 3, 0);
        System.out.println("测试1: " + calculate(t1Start, t1End) + " 元");  // 6.0

        // 测试2：周五 00:00 ~ 周六 00:00 （跨越两个计费日）
        LocalDateTime t2Start = LocalDateTime.of(2026, 7, 17, 0, 0);
        LocalDateTime t2End   = LocalDateTime.of(2026, 7, 18, 0, 0);
        System.out.println("测试2: " + calculate(t2Start, t2End) + " 元");  // 8 + 25 = 33.0

        // 测试3：周五 03:00 ~ 周六 05:00 （跨越三个片段）
        LocalDateTime t3Start = LocalDateTime.of(2026, 7, 17, 3, 0);
        LocalDateTime t3End   = LocalDateTime.of(2026, 7, 18, 5, 0);
        System.out.println("测试3: " + calculate(t3Start, t3End) + " 元");  // 2 + 25 + 2 = 29.0

        // 测试4：周六 04:00 ~ 周日 04:00 （整好24小时，周末封顶）
        LocalDateTime t4Start = LocalDateTime.of(2026, 7, 18, 4, 0);  // 周六
        LocalDateTime t4End   = LocalDateTime.of(2026, 7, 19, 4, 0);
        System.out.println("测试4: " + calculate(t4Start, t4End) + " 元");  // 30.0

        // 测试5：跨周，周一到周三，验证不同封顶
        LocalDateTime t5Start = LocalDateTime.of(2026, 7, 20, 5, 0);  // 周一
        LocalDateTime t5End   = LocalDateTime.of(2026, 7, 22, 3, 0);  // 周三
        System.out.println("测试5: " + calculate(t5Start, t5End) + " 元");
    }
}