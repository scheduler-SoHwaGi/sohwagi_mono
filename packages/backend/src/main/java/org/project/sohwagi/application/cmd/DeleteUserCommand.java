package org.project.sohwagi.application.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;
import org.project.sohwagi.domain.UserDetails;

public record DeleteUserCommand(
    @NotNull
    UserDetails userDetails,

    @NotNull
    String refreshToken
) {

  @Builder
  public DeleteUserCommand(
      UserDetails userDetails,
      String refreshToken
  ){
    this.userDetails = userDetails;
    this.refreshToken = refreshToken;
    validate(this);
  }

}
