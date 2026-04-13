package org.project.sohwagi.presentation.res;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;

public record LoginRes(
    @NotNull String accessToken,

    @NotNull String refreshToken
){
  @Builder
  public LoginRes(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    validate(this);
  }

}
