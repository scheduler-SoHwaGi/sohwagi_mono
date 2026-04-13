package org.project.sohwagi.application.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import static org.project.sohwagi.common.validation.Validation.validate;

public record AppleLoginCommand (
    @NotNull(message = "authorizationcode is required")
    String authorizationCode,

    @NotNull(message = "userName is required")
    String userName
){
  @Builder
  public AppleLoginCommand(
      String authorizationCode,
      String userName
  ){
    this.authorizationCode = authorizationCode;
    this.userName = userName;
    validate(this);
  }

}
