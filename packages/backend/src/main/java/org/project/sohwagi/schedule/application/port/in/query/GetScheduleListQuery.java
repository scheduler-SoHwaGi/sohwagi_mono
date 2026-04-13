package org.project.sohwagi.schedule.application.port.in.query;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;

public record GetScheduleListQuery(
	@NotNull(message = "userId is required")
	Long userId
) {
	@Builder
	public GetScheduleListQuery(
		Long userId
	) {
		this.userId = userId;
		validate(this);
	}

}
