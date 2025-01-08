package com.numo.domain.base;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Embeddable
@Getter
public class BaseDate {
    private int year;
    private int month;
    private int day;
    private int week;

    public BaseDate() {
        LocalDate localDate = LocalDate.now();
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();
        this.week = getCurrentWeekOfMonth(localDate);
    }

    public int getCurrentWeekOfMonth(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);

        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 1주차로 계산
        if (weekOfMonth == 0) {
            weekOfMonth++;
        }

        return weekOfMonth;
    }
}
