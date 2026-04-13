package org.project.sohwagi.application.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.project.sohwagi.domain.UserDetails;

public record SaveFcmTokenCommand(
    @NotNull(message = "fcmToken is required")
    String fcmToken,
    @NotNull(message = "user is required")
    UserDetails userDetails
) {

  @Builder
  public SaveFcmTokenCommand(
      String fcmToken,
      UserDetails userDetails
  ) {
    this.fcmToken = fcmToken;
    this.userDetails = userDetails;
  }
}
