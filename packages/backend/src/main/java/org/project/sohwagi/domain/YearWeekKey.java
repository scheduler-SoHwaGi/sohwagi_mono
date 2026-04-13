package org.project.sohwagi.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

public record YearWeekKey(int year, int weekOfMonth, LocalDate startOfWeek) implements Comparable<YearWeekKey> {

  public static YearWeekKey from(int year, int month, int day) {
    LocalDate date = LocalDate.of(year, month, day);
    WeekFields wf = WeekFields.of(DayOfWeek.MONDAY, 1);
    int week = date.get(wf.weekOfMonth());
    LocalDate start = date.with(wf.dayOfWeek(), DayOfWeek.MONDAY.getValue());
    return new YearWeekKey(year, week, start);
  }

  public String toLabel() {
    return switch (weekOfMonth) {
      case 1 -> "첫째주";
      case 2 -> "둘째주";
      case 3 -> "셋째주";
      case 4 -> "넷째주";
      case 5 -> "다섯째주";
      default -> weekOfMonth + "주차";
    };
  }

  public String toPeriodString() {
    String start = startOfWeek.format(DateTimeFormatter.ofPattern("MM.dd"));
    String end = startOfWeek.plusDays(6).format(DateTimeFormatter.ofPattern("MM.dd"));
    return start + " - " + end;
  }

  @Override
  public int compareTo(YearWeekKey o) {
    return this.startOfWeek.compareTo(o.startOfWeek);
  }
}