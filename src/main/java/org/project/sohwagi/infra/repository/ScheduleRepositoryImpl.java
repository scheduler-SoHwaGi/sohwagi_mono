package org.project.sohwagi.infra.repository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.common.PersistenceAdapter;
import org.project.sohwagi.domain.Schedule;
import org.project.sohwagi.domain.ScheduleRepository;
import org.project.sohwagi.domain.ScheduleType;
import org.project.sohwagi.infra.jpa.ScheduleJpaRepository;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

	private final ScheduleJpaRepository scheduleJpaRepository;

	@Override
	public Schedule saveSchedule(Schedule schedule) {
		long startTime = System.currentTimeMillis();

		Schedule savedSchedule = scheduleJpaRepository.save(schedule);

		log.info("db saved schedule in {} ms", System.currentTimeMillis() - startTime);
		return savedSchedule;
	}

	@Override
	public List<Schedule> loadSchedulesByUserId(Long userId) {
		return scheduleJpaRepository.findAllByUserIdOrderByMonthAscDayAsc(userId);
	}

	@Override
	public Schedule loadScheduleById(Long scheduleId) {
		return scheduleJpaRepository.findById(scheduleId)
			.orElseThrow(() -> new EntityNotFoundException("해당 스케줄은 존재하지 않습니다."));
	}

	@Override
	public void deleteSchedule(Schedule schedule) {
		scheduleJpaRepository.delete(schedule);
	}

	@Override
	public List<Schedule> findAllByUserIdAndYearAndMonth(Long userId, int year, int month) {
		return scheduleJpaRepository.findALlByUserIdAndYearAndMonth(userId, year, month);
    }

	@Override
	public List<Object[]> findCountByDateBetween(LocalDate start, LocalDate end) {
		return List.of();
	}

	@Override
	public long countByYearAndMonthAndDay(Long userId, int year, int month, int day) {
		return scheduleJpaRepository.countByUserIdAndYearAndMonthAndDay(userId, year, month, day);
	}

	@Override
	public List<Schedule> findAllByUserIdAndYearAndMonthAndDay(
			Long userId,
			int year,
			int month,
			int day) {
		return scheduleJpaRepository.findAllByUserIdAndYearAndMonthAndDayOrderByAmPmAscHourAscMinuteAsc(
				userId,
				year,
				month,
				day
		);
	}

	@Override
	public List<Schedule> findTodaySchedules(LocalDate today) {
		return scheduleJpaRepository.findAllByYearAndMonthAndDayAndType(
			today.getYear(), today.getMonthValue(), today.getDayOfMonth(), ScheduleType.SCHEDULE);
	}

	@Override
	public Schedule findScheduleById(Long scheduleId) {
		return scheduleJpaRepository.findById(scheduleId).orElseThrow(
				() -> new EntityNotFoundException("해당 일정은 존재하지 않습니다.")
		);
	}

	@Override
	public List<Schedule> findSchedulesByUserIdAndYearAndMonthAndDay(
			Long userId,
			int year,
			int month,
			int day) {
		return scheduleJpaRepository.findAllByUserIdAndYearAndMonthAndDay(userId, year, month, day);
	}

	@Override
	public List<Schedule> findAllByUserIdAndYmdBetween(
		Long userId, int fromYmd, int toYmd) {
		return scheduleJpaRepository.findAllByUserIdAndYmdBetween(userId, fromYmd, toYmd);
	}

	@Override
	public List<Schedule> findSchedulesNotifiedAt(
		LocalDateTime from, LocalDateTime to) {
		return scheduleJpaRepository.findAllByTypeAndNotifiedAt(ScheduleType.SCHEDULE, from, to);
	}
}
