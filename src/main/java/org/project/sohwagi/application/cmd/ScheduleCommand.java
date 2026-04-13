package org.project.sohwagi.application.cmd;

import java.time.LocalDate;
import org.project.sohwagi.domain.ScheduleType;
import org.project.sohwagi.infra.llm.LlmResult;

public class ScheduleCommand {

  public record ScheduleCountCommand(
      LocalDate start,
      LocalDate end,
      Long userId) {
    public static ScheduleCountCommand from(Long userId, LocalDate start, LocalDate end) {
      return new ScheduleCountCommand(start, end, userId);
    }
  }

  public record SchedulesGetOnDate(
      int year,
      int month,
      int day,
      Long userId
  ) {
    public static SchedulesGetOnDate from(int year, int month, int day, Long userId) {
      return new SchedulesGetOnDate(year, month, day, userId);
    }
  }

  public record ScheduleCreateByTextCommand(
      String text,
      Long userId
  ) { }

  public record ScheduleCreateCommand(
      String title,
      Integer year,
      Integer month,
      Integer day,
      String dayOfWeek,
      Integer hour,
      Integer minute,
      String ampm,
      ScheduleType type,
      Long userId
  ) {
    public static ScheduleCreateCommand from(LlmResult llmResult, Long userId) {
      return new ScheduleCreateCommand(
          llmResult.title(),
          llmResult.year(),
          llmResult.month(),
          llmResult.day(),
          llmResult.dayOfWeek(),
          llmResult.hour(),
          llmResult.minute(),
          llmResult.ampm(),
          ScheduleType.valueOf(llmResult.type()),
          userId
      );
    }
  }

  public record ScheduleCheckCommand(Long userId) { }
}
