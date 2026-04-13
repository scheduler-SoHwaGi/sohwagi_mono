package org.project.sohwagi.application.info;

import java.util.List;

public class ScheduleInfo {

  public record ScheduleCountInfo(String date, int counts, Status status) { }

  public record ScheduleCountsInfo(List<ScheduleCountInfo> scheduleCounts) {}

  public record TodoTypeInfo(
      Long scheduleId,
      String title,
      boolean checked
  ) { }

  public record ScheduleTypeInfo(
      Long scheduleId,
      String title,
      String time,
      boolean checked
  ) { }

  public record ScheduleDetailOnDateInfo(
      List<TodoTypeInfo> todoTypeInfoList,
      List<ScheduleTypeInfo> scheduleTypeInfoList
  ) { }
}
