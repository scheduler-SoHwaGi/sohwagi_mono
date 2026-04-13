package org.project.sohwagi.infra.apple;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleOAuthInfoRes(
    @JsonProperty("sub")
    String subject,

    @JsonProperty("email")
    String email,

    @JsonProperty("refreshToken")
    String refreshToken

) {

}
