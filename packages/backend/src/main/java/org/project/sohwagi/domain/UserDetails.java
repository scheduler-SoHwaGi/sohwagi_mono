package org.project.sohwagi.domain;

public record UserDetails(
    Long id,
    String fcmToken,
    String userName,
    String oauthProvider,
    String email,
    boolean isDeleted,
    boolean hasSchedule) {

  public User toEntity() {
    return User.builder()
        .id(id)
        .fcmToken(fcmToken)
        .userName(userName)
        .oauthProvider(oauthProvider)
        .email(email)
        .isDeleted(isDeleted)
        .hasSchedule(hasSchedule)
        .build();
  }

  public static UserDetails from(User user) {
    return new UserDetails(
        user.getId(),
        user.getFcmToken(),
        user.getUserName(),
        user.getOauthProvider(),
        user.getEmail(),
        user.isDeleted(),
        user.isHasSchedule()
    );
  }
}
