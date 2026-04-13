package org.project.sohwagi.infra.llm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.common.exception.CustomException;
import org.project.sohwagi.common.exception.ErrorCode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LLMClientImpl implements LlmClient {

  private final ChatClient chatClient;
  private final Resource promptResource;

  public LLMClientImpl(
      ChatClient.Builder chatClientBuilder,
      @Value("classpath:/prompts/add-schedule-prompt.txt") Resource promptResource) {
    this.chatClient = chatClientBuilder.build();
    this.promptResource = promptResource;
  }


  @Override
  public LlmResult extractScheduleInformation(String input) {
    try {
      String nowText = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분")
        .format(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
      Map<String, Object> model = Map.of(
        "now", nowText,
        "input", input
      );
      return chatClient
          .prompt()
          .user(userSpec -> userSpec
              .text(promptResource)
              .params(model))
          .call()
          .entity(LlmResult.class);
    } catch (NonTransientAiException e) {
      throw new CustomException(ErrorCode.INTERNAL_LLM_SERVER_ERROR);
    }
  }
}
