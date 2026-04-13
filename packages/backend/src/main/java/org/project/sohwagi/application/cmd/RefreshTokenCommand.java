package org.project.sohwagi.application.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;

public record RefreshTokenCommand(
    @NotNull
    String refreshToken
) {
  @Builder
  public RefreshTokenCommand(
      String refreshToken
  ){
    this.refreshToken = refreshToken;
    validate(this);
  }
}
