package org.project.sohwagi.domain;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.project.sohwagi.infra.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final UserJpaRepository userJpaRepository;

  public UserRepository(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  public void delete(UserDetails userDetails) {
    User user = userJpaRepository.findById(userDetails.id())
        .orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));

    userJpaRepository.delete(user);
  }

  public void update(User user) {
    userJpaRepository.saveAndFlush(user);
  }

  public User findById(Long id) {

    return userJpaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));
  }

  public User save(User user) {
    return userJpaRepository.save(user);
  }

  public List<User> findActiveUsersWithoutSchedulesBetweenYmd(int startYmd, int endYmd) {
    return userJpaRepository.findActiveUsersWithoutSchedulesBetweenYmd(startYmd, endYmd);
  }

  public List<User> findAllUserByIdIn(Set<Long> ids) {
    return userJpaRepository.findAllByIdIn(ids);
  }

  public Optional<User> findTestUser(String userName) {
    return userJpaRepository.findByUserName(userName);
  }

  public List<User> findDistinctUsersByFcmToken() {
    return userJpaRepository.findDistinctUsersByFcmToken();
  }
}
