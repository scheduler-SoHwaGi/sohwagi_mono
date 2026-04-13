package org.project.sohwagi.application.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;

public record DeleteScheduleCommand(
	@NotNull(message = "scheduleId is required")
	Long scheduleId,
	@NotNull(message = "userId is required")
	Long userId
) {
	@Builder
	public DeleteScheduleCommand(
		Long scheduleId,
		Long userId
	) {
		this.scheduleId = scheduleId;
		this.userId = userId;
		validate(this);
	}

}
