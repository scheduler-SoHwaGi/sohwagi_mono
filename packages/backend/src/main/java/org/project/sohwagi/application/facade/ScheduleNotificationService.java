package org.project.sohwagi.application.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.application.service.ScheduleService;
import org.project.sohwagi.application.service.UserService;
import org.project.sohwagi.domain.Schedule;
import org.project.sohwagi.domain.ScheduleType;
import org.project.sohwagi.domain.User;
import org.project.sohwagi.infra.firebase.FirebaseMessagingClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleNotificationService {

  private final ScheduleService scheduleService;
  private final FirebaseMessagingClient firebaseMessagingClient;
  private final UserService userService;

  public ScheduleNotificationService(ScheduleService scheduleService,
    FirebaseMessagingClient firebaseMessagingClient, UserService userService) {
    this.scheduleService = scheduleService;
    this.firebaseMessagingClient = firebaseMessagingClient;
    this.userService = userService;
  }

  public void sendDailyScheduleNotifications() {
    LocalDate today = LocalDate.now();

    List<Schedule> todaySchedules = scheduleService.findTodaySchedules(today).stream()
      .filter(s -> s.getType().equals(
        ScheduleType.SCHEDULE)).toList();
    if (todaySchedules.isEmpty()) {
      return;
    }

    Map<Long, List<Schedule>> scheduleMap = todaySchedules.stream()
      .collect(Collectors.groupingBy(Schedule::getUserId));
    Set<Long> userIds = scheduleMap.keySet();
    Map<Long, User> userMap = userService.findAllUserByIdIn(userIds).stream()
      .collect(Collectors.toMap(User::getId, user -> user));

    for (Map.Entry<Long, List<Schedule>> entry : scheduleMap.entrySet()) {
      Long userId = entry.getKey();
      User user = userMap.get(userId);
      if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) {
        continue;
      }
      List<Schedule> schedules = entry.getValue();
      String title = String.format("오늘 일정 %d개다 햄+_+", schedules.size());
      String body = String.format(
        "오늘 %s %02d:%02d에 %s이(가) 있어요!",
        schedules.get(0).getAmPm(),
        schedules.get(0).getHour(),
        schedules.get(0).getMinute(),
        schedules.get(0).getTitle());

      firebaseMessagingClient.sendMessage(user.getFcmToken(), title, body);
    }
  }

  public void sendScheduleRegistrationNotifications() {
    LocalDate today = LocalDate.now();
    LocalDate sevenDaysAgo = today.minusDays(6);
    List<User> targets = userService.findUsersWithoutSchedulesInLastWeek(today, sevenDaysAgo);
    for (User target : targets) {
      String title = "지금 기억나는 일정 빠르게 등록하라 햄+_+";
      String body = "메모하듯이 한 문장으로 빠르게 입력해보세요!";

      firebaseMessagingClient.sendMessage(target.getFcmToken(), title, body);
    }
  }

  public void sendDailyScheduleRegistrationNotifications() {
    List<User> users = userService.findDistinctUsersByFcmToken();
    for (User user : users) {
      if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) {
        continue;
      }
      String name = user.getUserName().split(" ")[0];
      String title = name + "햄! 지금 생각난 일정 한 줄로 남겨두자 햄!";
      String body = "지금 떠오른 그 일정을 한 줄로 남겨보세요!";
      firebaseMessagingClient.sendMessage(user.getFcmToken(), title, body);
    }
  }

  public void sendSchedule15mBeforeNotifications() {
    List<Schedule> schedules = scheduleService.findSchedulesToNotify();
    for (Schedule schedule : schedules) {
      User user = userService.findById(schedule.getUserId());
      if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) {
        continue;
      }
      String name = user.getUserName().split(" ")[0];
      String title = name + "햄! 잠깐만, 이 일정 잊지 않았지?";
      String body = "15분 후에 일정이 있어! 미리 준비하면 딱 좋아햄!";
      firebaseMessagingClient.sendMessage(user.getFcmToken(), title, body);
      scheduleService.markScheduleAsNotified(schedule.getId());
    }
  }
}
