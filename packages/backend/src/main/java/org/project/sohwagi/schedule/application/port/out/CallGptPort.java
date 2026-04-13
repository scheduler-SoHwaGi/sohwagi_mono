package org.project.sohwagi.schedule.application.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.project.sohwagi.presentation.req.ScheduleRequest;

public interface CallGptPort {

	ScheduleRequest callGptForTextSchedule(String prompt) throws JsonProcessingException;

}
