package org.project.sohwagi.presentation;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.application.facade.ScheduleNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DailyNotificationScheduler {

  private final ScheduleNotificationService scheduleNotificationService;

  public DailyNotificationScheduler(ScheduleNotificationService scheduleNotificationService){
    this.scheduleNotificationService = scheduleNotificationService;
  }

  @Scheduled(cron = "0 00 09 * * *", zone = "Asia/Seoul")
  public void notifyTodaySchedules() {
    log.info("📅 [DailyNotificationScheduler] start");
    scheduleNotificationService.sendDailyScheduleNotifications();
  }

  @Scheduled(cron = "0 47 12 * * WED", zone = "Asia/Seoul")
  public void notifyScheduleRegistrationPrompt() {
    log.info("📅 [DailyNotificationScheduler] start");
    scheduleNotificationService.sendScheduleRegistrationNotifications();
  }

  @Scheduled(cron = "0 16 08 * * *", zone = "Asia/Seoul")
  public void notifyDailyScheduleRegistration() {
    log.info("📅 [DailyNotificationScheduler] start");
    scheduleNotificationService.sendDailyScheduleRegistrationNotifications();
  }

  @Scheduled(fixedRate = 60000)
  public void notifySchedule15mBefore() {
    scheduleNotificationService.sendSchedule15mBeforeNotifications();
  }
}
