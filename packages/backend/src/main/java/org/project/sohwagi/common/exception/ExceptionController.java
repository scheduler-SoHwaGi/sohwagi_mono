package org.project.sohwagi.common.exception;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE, errorMessage);
  }

  @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
    return ErrorResponse.toResponseEntity(ErrorCode.ENTITY_NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
    return ErrorResponse.toResponseEntity(ErrorCode.DUPLICATE_RESOURCE, e.getMessage());
  }

  @ExceptionHandler({JwtException.class, AccessDeniedException.class})
  public ResponseEntity<ErrorResponse> handleJwtException(Exception e) {
    return ErrorResponse.toResponseEntity(ErrorCode.ACCESS_DENIED, e.getMessage());
  }

  @ExceptionHandler(OAuthRequestException.class)
  public ResponseEntity<ErrorResponse> handleOAuthException(OAuthRequestException e) {
    return ErrorResponse.toResponseEntityForOAuth(
        e.getStatus(), ErrorCode.OAUTH_REQUEST_FAILED.getCode(), e.getMessage());
  }

  @ExceptionHandler(RefreshTokenException.class)
  public ResponseEntity<ErrorResponse> handleRefreshTokenException(Exception e) {
    return ErrorResponse.toResponseEntity(ErrorCode.INVALID_REFRESH_TOKEN, e.getMessage());
  }

  @ExceptionHandler(AccessTokenException.class)
  public ResponseEntity<ErrorResponse> handleAccessTokenException(AccessTokenException e) {
    return ErrorResponse.toResponseEntityForToken(
        ErrorCode.INVALID_ACCESS_TOKEN, e.getMessage(), e.getNewAccessToken());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParam(
      MissingServletRequestParameterException e) {
    String message = String.format("'%s' 파라미터는 필수입니다.", e.getParameterName());
    return ErrorResponse.toResponseEntity(ErrorCode.MISSING_REQUEST_PARAMETER, message);
  }

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    return ErrorResponse.toResponseEntity(e.getErrorCode());
  }
}

