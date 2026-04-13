# ADR : 스케줄 생성 latency 최적화

## 작성일

2025-10-19

---

## 컨텍스트

스케줄 등록 요청(`createScheduleByText()`)의 응답 시간이 평균 2.3 ~ 2.8초로, 사용자 체감 지연이 발생하고 있었다.

호출 흐름은 다음과 같았다:

```java
createScheduleByText()
  → llmClient.extractScheduleInformation()  ← ⚠️ 주요 지연 구간
  → scheduleService.createScheduleByText()
  → user.markHasScheduleTrue()
```

분석 결과, 전체 처리 시간의 약 90% 이상이 LLM 호출(inference latency)에서 발생했으며,
DB I/O 및 기타 연산은 전체 지연의 10% 미만이었다.

따라서 LLM 응답 속도 최적화를 위해 모델 변경을 검토하였다.

---

## 결정

* **LLM 모델을 OpenAI GPT-4o에서 Vertex AI Gemini 2.5 Flash-Lite로 교체하였다.**
* 기존 GPT-4o는 평균 2초 이상의 응답 지연이 지속되었으며,
  Gemini 2.5 Flash-Lite는 첫 요청만 약 3초가 소요되지만 이후 요청은 대부분 **1초 미만**으로 응답되었다.
* Gemini Flash-Lite는 한국어 자연어 시간 표현 파싱 성능도 우수하여,
  모델 교체만으로 품질 및 속도 모두 개선 효과를 얻을 수 있었다.
* 모델 교체를 위해 `spring-ai-starter-model-openai` 의존성을 제거하고
  `spring-ai-starter-model-vertex-ai-gemini`를 추가하였다.

---

## 결과

* **응답 시간:** 평균 2.3–2.8 초 → 0.8 ~ 1.0 초로 단축됨
* **시스템 영향:** 코드 변경 최소화, build.gradle에서 의존성 전환만 수행
* **정확도:** 숫자/자연어 기반 시간 표현 모두 안정적으로 처리됨
* **결과적으로 전체 일정 등록 API의 체감 응답 속도가 약 60% 이상 개선되었다.**

---

## 대안

| 대안                                 | 설명                    | 단점                 |
| ---------------------------------- | --------------------- | ------------------ |
| (A) GPT-4o 유지                      | 수정 없이 기존 유지           | 평균 2초대 응답 지속       |
| (B) Gemini 1.5 Flash               | 응답 속도 우수              | 시간 표현 처리 불안정       |
| (C) **Gemini 2.5 Flash-Lite (선택)** | 응답 < 1 초, 안정적인 한국어 처리 | 첫 요청만 약간 지연 (3 초대) |

---

## 관련 문서

* PR `스케줄 등록 시간 개선 – LLM 호출 시간 최적화`
* 관련 Commit :
    * `#47 refactor : LLM 모델 변경`
    * `#47 feat : 도메인 규칙 Schedule Entity에도 적용`
