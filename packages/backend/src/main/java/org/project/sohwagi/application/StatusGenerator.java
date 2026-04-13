package org.project.sohwagi.application;

import java.util.List;
import org.project.sohwagi.application.info.Status;
import org.project.sohwagi.domain.Schedule;

public class StatusGenerator {

  public static Status generateStatus(int counts, List<Schedule> schedules) {

    int numOfCheckedSchedules = schedules.stream().filter(Schedule::isChecked).toList().size();

    int numOfSchedulesRemaining = schedules.size() - numOfCheckedSchedules;

    if (counts == 0) {
      return Status.NONE;
    } else if (numOfSchedulesRemaining > 0) {
      return Status.IN_PROGRESS;
    } else if (numOfSchedulesRemaining == 0) {
      return Status.DONE;
    }

    return null;
  }

  public static int generateCounts(List<Schedule> schedules) {

    return schedules.size() - schedules.stream().filter(Schedule::isChecked).toList().size();
  }

}
