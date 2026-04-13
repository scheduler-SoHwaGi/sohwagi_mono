package org.project.sohwagi.schedule.application.port.in.usecase;

import java.util.List;
import org.project.sohwagi.presentation.res.ScheduleResponse;
import org.project.sohwagi.schedule.application.port.in.query.GetScheduleListQuery;

public interface GetScheduleUseCase {

	List<ScheduleResponse> getScheduleList(GetScheduleListQuery query);

}
