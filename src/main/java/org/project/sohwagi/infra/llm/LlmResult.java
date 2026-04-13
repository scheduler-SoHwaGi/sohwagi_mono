package org.project.sohwagi.infra.llm;

public record LlmResult(
    String title,
    Integer year,
    Integer month,
    Integer day,
    String dayOfWeek,
    Integer hour,
    Integer minute,
    String ampm,
    String type
) { }
