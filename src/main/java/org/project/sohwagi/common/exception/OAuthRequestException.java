package org.project.sohwagi.common.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OAuthRequestException extends RuntimeException{
  private final HttpStatus status;
  public OAuthRequestException(HttpStatus status, String rawMessage) {
    super(parseErrorDescription(rawMessage));
    this.status = status;
  }
  private static String parseErrorDescription(String rawMessage) {
    try {
      // JSON 메시지를 추출
      int jsonStartIndex = rawMessage.indexOf("{");
      if (jsonStartIndex == -1) {
        return rawMessage; // JSON이 없으면 원본 메시지 반환
      }
      String jsonString = rawMessage.substring(jsonStartIndex, rawMessage.length() - 1);

      // JSON 파싱하여 error_description 추출
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(jsonString);
      if (root.has("error_description")) {
        return root.get("error_description").asText();
      }
      return rawMessage; // error_description이 없으면 원본 메시지 반환
    } catch (Exception e) {
      return rawMessage; // 파싱 실패 시 원본 메시지 반환
    }
  }
}
