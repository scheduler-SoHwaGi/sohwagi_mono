package org.project.sohwagi.application.facade;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.project.sohwagi.application.StatusGenerator;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCheckCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCountCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCreateByTextCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCreateCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.SchedulesGetOnDate;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleCountInfo;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleCountsInfo;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleDetailOnDateInfo;
import org.project.sohwagi.application.info.ScheduleInfo.ScheduleTypeInfo;
import org.project.sohwagi.application.info.ScheduleInfo.TodoTypeInfo;
import org.project.sohwagi.application.service.ScheduleService;
import org.project.sohwagi.application.service.UserService;
import org.project.sohwagi.common.exception.CustomException;
import org.project.sohwagi.common.exception.ErrorCode;
import org.project.sohwagi.domain.Schedule;
import org.project.sohwagi.domain.ScheduleType;
import org.project.sohwagi.domain.User;
import org.project.sohwagi.infra.llm.LlmClient;
import org.project.sohwagi.infra.llm.LlmResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleFacadeService {

  private final LlmClient llmClient;
  private final ScheduleService scheduleService;
  private final UserService userService;

  public ScheduleCountsInfo generateScheduleSummaries(ScheduleCountCommand cmd) {
    Map<String, List<Schedule>> result = scheduleService.getSchedulesGroupedByDate(cmd);

    List<ScheduleCountInfo> infos = result.entrySet().stream()
        .map(entry -> new ScheduleCountInfo(
            entry.getKey(),
            StatusGenerator.generateCounts(entry.getValue()),
            StatusGenerator.generateStatus(entry.getValue().size(), entry.getValue()))
        ).toList();

    return new ScheduleCountsInfo(infos);
  }

  public ScheduleDetailOnDateInfo getSchedulesOnDate(SchedulesGetOnDate cmd) {
    List<Schedule> schedules = scheduleService.getSchedulesOnDate(cmd);
    Map<Boolean, List<Schedule>> partitionedSchedules = schedules.stream()
        .collect(Collectors.partitioningBy(s -> s.getType() == ScheduleType.SCHEDULE));
    List<ScheduleTypeInfo> scheduleTypeInfoList = partitionedSchedules.get(true).stream()
        .map(schedule -> new ScheduleTypeInfo(
            schedule.getId(),
            schedule.getTitle(),
            schedule.getAmPm() + " " + schedule.getHour() + "시 " + String.format("%02d", schedule.getMinute()) + "분",
            schedule.getChecked()
        ))
        .toList();
    List<TodoTypeInfo> todoTypeInfoList = partitionedSchedules.get(false).stream()
        .map(schedule -> new TodoTypeInfo(
            schedule.getId(),
            schedule.getTitle(),
            schedule.getChecked()
        ))
        .toList();
    return new ScheduleDetailOnDateInfo(todoTypeInfoList, scheduleTypeInfoList);
  }

  @Transactional
  public Long createScheduleByText(ScheduleCreateByTextCommand cmd) {
    LlmResult scheduleInformation = llmClient.extractScheduleInformation(cmd.text());
    ScheduleCreateCommand scheduleCreateCommand = ScheduleCreateCommand.from(
        scheduleInformation, cmd.userId());
    Long savedScheduleId = scheduleService.createScheduleByText(scheduleCreateCommand);
    User user = userService.findById(cmd.userId());
    user.markHasScheduleTrue();
    return savedScheduleId;
  }

  @Transactional
  public void checkSchedule(ScheduleCheckCommand cmd) {
    scheduleService.checkSchedule(cmd);
  }

}
