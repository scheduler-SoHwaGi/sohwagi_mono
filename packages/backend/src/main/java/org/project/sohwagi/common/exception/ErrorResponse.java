package org.project.sohwagi.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private final int status;
  private final String code;
  private final String message;
  private final String newAccessToken;

  public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.builder()
            .status(errorCode.getStatus().value())
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build()
        );
  }

  public static ResponseEntity<ErrorResponse> toResponseEntity(
      ErrorCode errorCode, String message) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.builder()
            .status(errorCode.getStatus().value())
            .code(errorCode.getCode())
            .message(message)
            .build()
        );
  }

  public static ResponseEntity<ErrorResponse> toResponseEntityForToken(
      ErrorCode errorCode, String message, String newAccessToken) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.builder()
            .status(errorCode.getStatus().value())
            .code(errorCode.getCode())
            .message(message)
            .newAccessToken(newAccessToken)
            .build()
        );
  }

  public static ResponseEntity<ErrorResponse> toResponseEntityForOAuth(
      HttpStatus status, String code, String message) {
    return ResponseEntity
        .status(status)
        .body(ErrorResponse.builder()
            .status(status.value())
            .code(code)
            .message(message)
            .build()
        );
  }
}
