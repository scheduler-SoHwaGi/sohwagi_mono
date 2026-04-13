package org.project.sohwagi.infra.apple;

import jakarta.validation.constraints.NotNull;
import static org.project.sohwagi.common.validation.Validation.validate;


public record AppleLoginRequest(
    @NotNull
    String authorizationCode,

    @NotNull
    String userName

    ) {
    public AppleLoginRequest(
        String authorizationCode,
        String userName
    ){
        this.authorizationCode = authorizationCode;
        this.userName = userName;
        validate(this);
    }
}
