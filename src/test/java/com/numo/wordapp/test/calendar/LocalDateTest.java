package com.numo.wordapp.test.calendar;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class LocalDateTest {
    public static String getCurrentWeekOfMonth(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);

        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 1주차로 계산
        if (weekOfMonth == 0) {
            weekOfMonth++;
        }

        return localDate.getMonthValue() + "월 " + weekOfMonth + "주차";
    }

    @Test
    void weekOfMonth() {
        LocalDate date = LocalDate.now();
        date = LocalDate.of(2024, 6,30);

        System.out.println(getCurrentWeekOfMonth(date));
    }
}
