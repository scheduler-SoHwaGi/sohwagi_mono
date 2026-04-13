package org.project.sohwagi.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // Common
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력 값입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 메소드입니다."),
  ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "항목을 찾을 수 없습니다."),
  INTERNAL_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 내부 오류가 발생했습니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근이 거부되었습니다."),
  MISSING_REQUEST_PARAMETER(
      HttpStatus.BAD_REQUEST, "C006", "필수 파라미터가 누락되었습니다."),
  DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C007", "이미 존재하는 데이터입니다."),

  // Schedule
  INVALID_SCHEDULE_TEXT_INPUT_VALUE(
      HttpStatus.BAD_REQUEST, "S001", "일정 내용을 작성해주세요."),
  INTERNAL_LLM_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "S002", "잠시후에 다시 시도해주세요"),
  LLM_RESULT_PARSING_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "S002", "AI 서버 응답을 파싱하는 데 실패했습니다."),

  // Auth
  INVALID_REFRESH_TOKEN(
      HttpStatus.UNAUTHORIZED, "A001", "리프레시 토큰이 유효하지 않습니다."),
  INVALID_ACCESS_TOKEN(
      HttpStatus.FORBIDDEN, "A002", "액세스 토큰이 유효하지 않거나 만료되었습니다."),
  OAUTH_REQUEST_FAILED(
      HttpStatus.BAD_REQUEST, "A003", "OAuth 요청 처리에 실패했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
