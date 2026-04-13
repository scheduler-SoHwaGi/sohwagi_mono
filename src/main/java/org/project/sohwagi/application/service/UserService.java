package org.project.sohwagi.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.project.sohwagi.application.cmd.SaveFcmTokenCommand;
import org.project.sohwagi.common.UseCase;
import org.project.sohwagi.domain.User;
import org.project.sohwagi.domain.UserDetails;
import org.project.sohwagi.domain.UserRepository;
import org.project.sohwagi.presentation.res.GetUserInfoRes;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void saveFcmToken(SaveFcmTokenCommand command) {
    User user = command.userDetails().toEntity();
    user.updateFcmToken(command.fcmToken());

    userRepository.update(user);
  }

  public void deleteUser(UserDetails userDetails) {

    userRepository.delete(userDetails);
  }

  public UserDetails loadUserById(Long id) {

    User user = userRepository.findById(id);

    return UserDetails.from(user);
  }

  public GetUserInfoRes getUserInfo(UserDetails userDetails) {
    String name = convertName(userDetails.userName());

    return new GetUserInfoRes(name, userDetails.email());
  }

  public User findById(Long userId) {
    return userRepository.findById(userId);
  }

  public User saveUser(String userName, String oauthProvider, String email) {
    User user = User.create(userName, oauthProvider, email);
    return userRepository.save(user);
  }

  public List<User> findUsersWithoutSchedulesInLastWeek(LocalDate today, LocalDate sevenDaysAgo) {
    int startYmd = sevenDaysAgo.getYear() * 10000
        + sevenDaysAgo.getMonthValue() * 100
        + sevenDaysAgo.getDayOfMonth();
    int endYmd = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth();
    return userRepository.findActiveUsersWithoutSchedulesBetweenYmd(startYmd, endYmd);
  }

  public List<User> findAllUserByIdIn(Set<Long> ids) {
    return userRepository.findAllUserByIdIn(ids);
  }

  public User getTestUser(String userName) {
    return userRepository.findTestUser(userName).orElseGet(
        () -> saveUser(userName, "TEST_PROVIDER", userName + "@test.com"));
  }

  public List<User> findDistinctUsersByFcmToken() {
    return userRepository.findDistinctUsersByFcmToken();
  }

  private String convertName(String name) {
    if (name.contains(" ")) {
      String[] parts = name.split(" ");
      if (parts.length == 2) {
        String firstName = parts[0];
        String lastName = parts[1];

        return lastName + firstName;
      }
    }
    return name;
  }
}
