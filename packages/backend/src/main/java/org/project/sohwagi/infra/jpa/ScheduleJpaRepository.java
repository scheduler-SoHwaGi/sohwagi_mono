package org.project.sohwagi.infra.jpa;

import java.time.LocalDateTime;
import java.util.List;
import org.project.sohwagi.domain.Schedule;
import org.project.sohwagi.domain.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findAllByUserIdOrderByMonthAscDayAsc(Long userId);

  List<Schedule> findALlByUserIdAndYearAndMonth(Long userId, Integer year, Integer month);

  long countByUserIdAndYearAndMonthAndDay(Long userId, int year, int month, int day);

  List<Schedule> findAllByUserIdAndYearAndMonthAndDayOrderByAmPmAscHourAscMinuteAsc(
      Long userId,
      int year,
      int month,
      int day
  );

  List<Schedule> findAllByYearAndMonthAndDayAndType(int year, int month, int day, ScheduleType type);

  List<Schedule> findAllByUserIdAndYearAndMonthAndDay(
      Long userId,
      Integer year,
      Integer month,
      int day
  );

  @Query(value = """
        SELECT s
        FROM Schedule s
        WHERE s.userId = :userId
          AND (s.year * 10000 + s.month * 100 + s.day) BETWEEN :fromYmd AND :toYmd
    """)
  List<Schedule> findAllByUserIdAndYmdBetween(
    @Param("userId") Long userId,
    @Param("fromYmd") int fromYmd,
    @Param("toYmd") int toYmd
  );

  @Query(value = """
        SELECT s
        FROM Schedule s
        WHERE s.type = :type
          AND s.notified = false
          AND s.notifiedAt >= :from
          AND s.notifiedAt < :to
  """)
  List<Schedule> findAllByTypeAndNotifiedAt(ScheduleType type, LocalDateTime from, LocalDateTime to);
}
