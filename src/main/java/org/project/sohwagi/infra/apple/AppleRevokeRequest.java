package org.project.sohwagi.infra.apple;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;

public record AppleRevokeRequest(
    @NotNull
    String authorizationCode
) {

  @Builder
  public AppleRevokeRequest(
      String authorizationCode
  ) {
    this.authorizationCode = authorizationCode;
    validate(this);
  }

}
