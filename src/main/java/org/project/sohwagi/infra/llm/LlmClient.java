package org.project.sohwagi.infra.llm;

public interface LlmClient {

  LlmResult extractScheduleInformation(String input);
}
