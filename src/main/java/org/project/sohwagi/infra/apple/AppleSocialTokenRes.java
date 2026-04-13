package org.project.sohwagi.infra.apple;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleSocialTokenRes(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("expires_in")
    Long expiresIn,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("id_token")
    String idToken
) {

}
