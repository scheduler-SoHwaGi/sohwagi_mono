package org.project.sohwagi.infra.jpa;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.project.sohwagi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<User, Long> {

  Optional<User> findByFcmToken(String fcmToken);

  Optional<User> findByUserName(String userName);

  @Query("SELECT u FROM User u WHERE u.isDeleted = false AND NOT EXISTS " +
      "(SELECT s FROM Schedule s WHERE s.userId = u.id AND " +
      "s.scheduleDate BETWEEN :start AND :end)")
  List<User> findActiveUsersWithoutSchedulesBetween(
      @Param("start") LocalDate start,
      @Param("end") LocalDate end);

  List<User> findAllByIdIn(Set<Long> ids);

  @Query("SELECT u FROM User u WHERE u.fcmToken IS NOT NULL GROUP BY u.fcmToken")
  List<User> findDistinctUsersByFcmToken();
}
