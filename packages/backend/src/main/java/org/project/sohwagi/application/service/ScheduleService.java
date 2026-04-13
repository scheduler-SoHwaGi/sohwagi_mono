package org.project.sohwagi.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.application.cmd.DeleteScheduleCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCheckCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCountCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCreateCommand;
import org.project.sohwagi.application.cmd.ScheduleCommand.SchedulesGetOnDate;
import org.project.sohwagi.common.UseCase;
import org.project.sohwagi.domain.Schedule;
import org.project.sohwagi.domain.ScheduleRepository;
import org.project.sohwagi.schedule.application.port.in.usecase.DeleteScheduleUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@Service
@RequiredArgsConstructor
public class ScheduleService
    implements DeleteScheduleUseCase {

  private final ScheduleRepository scheduleRepository;

  @Transactional
  public Long createScheduleByText(ScheduleCreateCommand command) {
    log.info("Create schedule by text 시작");
    Schedule newSchedule = new Schedule(
        command.title(),
        command.userId(),
        command.year(),
        command.month(),
        command.day(),
        command.dayOfWeek(),
        command.ampm(),
        command.hour(),
        command.minute(),
        command.type()
    );
    Schedule savedSchedule = scheduleRepository.saveSchedule(newSchedule);
    return savedSchedule.getId();
  }


  @Override
  @Transactional
  public void deleteSchedule(DeleteScheduleCommand command) {
    Schedule schedule = scheduleRepository.loadScheduleById(command.scheduleId());

    scheduleRepository.deleteSchedule(schedule);
  }

  @Transactional
  public void deleteScheduleByUserRevoke(Long userId) {
    List<Schedule> schedules = scheduleRepository.loadSchedulesByUserId(userId);

    for (Schedule schedule : schedules) {
      scheduleRepository.deleteSchedule(schedule);
    }
  }

  public Map<String, List<Schedule>> getSchedulesGroupedByDate(ScheduleCountCommand cmd) {
    int fromYmd = cmd.start().getYear() * 10000
      + cmd.start().getMonthValue() * 100
      + cmd.start().getDayOfMonth();
    int toYmd = cmd.end().getYear() * 10000
      + cmd.end().getMonthValue() * 100
      + cmd.end().getDayOfMonth();
    List<Schedule> schedules = scheduleRepository.findAllByUserIdAndYmdBetween(
      cmd.userId(),
      fromYmd,
      toYmd
    );
    Map<String, List<Schedule>> grouped = schedules.stream()
      .collect(Collectors.groupingBy(s -> s.getDate().toString()));
    long days = ChronoUnit.DAYS.between(cmd.start(), cmd.end()) + 1;
    return Stream.iterate(cmd.start(), d -> d.plusDays(1))
      .limit(days)
      .collect(Collectors.toMap(
        LocalDate::toString,
        d -> grouped.getOrDefault(d.toString(), Collections.emptyList()),
        (a, b) -> a,
        LinkedHashMap::new
      ));
  }

  public List<Schedule> getSchedulesOnDate(SchedulesGetOnDate cmd) {
    return scheduleRepository.findAllByUserIdAndYearAndMonthAndDay(
        cmd.userId(),
        cmd.year(),
        cmd.month(),
        cmd.day());
  }

  public List<Schedule> findTodaySchedules(LocalDate today) {
    return scheduleRepository.findTodaySchedules(today);
  }

  public void checkSchedule(ScheduleCheckCommand cmd) {
    Schedule schedule = scheduleRepository.findScheduleById(cmd.userId());

    schedule.checkSchedule(schedule.getChecked());
  }

  public List<Schedule> findSchedulesToNotify() {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime nowMinus1m = now.minusMinutes(1);
    return scheduleRepository.findSchedulesNotifiedAt(nowMinus1m, now);
  }

  @Transactional
  public void markScheduleAsNotified(Long scheduleId) {
    Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
    schedule.markAsNotified();
  }
}