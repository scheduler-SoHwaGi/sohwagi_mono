package org.project.sohwagi.presentation.res;

import java.util.List;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleCountInfo;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleCountsInfo;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleDetailOnDateInfo;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleTypeInfo;
import org.project.sohwagi.application.info.ScheduleInfo.TodoTypeInfo;
import org.project.sohwagi.application.info.Status;

public class ScheduleResponse {

  public record ScheduleCountResponse(String date, int counts, Status status) {

    public static ScheduleCountResponse from(ScheduleCountInfo info) {
      return new ScheduleCountResponse(info.date(), info.counts(), info.status());
    }
  }

  public record ScheduleCountsResponse(List<ScheduleCountResponse> scheduleCounts) {

    public static ScheduleCountsResponse from(ScheduleCountsInfo info) {
      return new ScheduleCountsResponse(
          info.scheduleCounts().stream().map(ScheduleCountResponse::from).toList()
      );
    }
  }

  public record ScheduleDetailOnDateResponse(
      List<TodoTypeInfo> todo,
      List<ScheduleTypeInfo> schedules
  ) {

    public static ScheduleDetailOnDateResponse from(ScheduleDetailOnDateInfo info) {
      return new ScheduleDetailOnDateResponse(info.todoTypeInfoList(),info.scheduleTypeInfoList());
    }
  }

}