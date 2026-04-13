package org.project.sohwagi.schedule.application.port.in.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.project.sohwagi.application.cmd.ScheduleCommand.ScheduleCreateByTextCommand;

public interface CreateScheduleUseCase {

	Long createScheduleByText(ScheduleCreateByTextCommand command) throws JsonProcessingException;

}
