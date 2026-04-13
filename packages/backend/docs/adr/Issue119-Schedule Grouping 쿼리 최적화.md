# ADR : Schedule Grouping 쿼리 최적화

## 작성일

2025-10-19

## 컨텍스트

`ScheduleService#getSchedulesGroupedByDate()` 메서드는 조회 기간(`start` ~ `end`)의 일수만큼
`scheduleRepository.findSchedulesByUserIdAndYearAndMonthAndDay()`를 반복 호출하는 구조였다.

예를 들어, 30일 구간을 조회할 경우 동일한 SELECT 쿼리가 30회 실행되어,
기간이 길수록 쿼리 수가 선형적으로 증가(N+1 쿼리 패턴)하는 문제가 발생했다.

이는 응답 속도 저하 및 불필요한 DB 부하의 주요 원인이 되었으며,
대규모 데이터(예: 월간/분기별 조회) 시 성능 병목을 유발할 가능성이 있었다.

이에 따라, 단일 쿼리로 기간 전체를 조회하고 메모리에서 날짜별로 그룹화하는 방식으로 개선하고자 했다.

---

## 결정

* 기존의 **날짜별 반복 쿼리 호출 구조**를 **단일 기간 조회 쿼리 + 메모리 그룹화 구조**로 변경한다.
* `ScheduleJpaRepository`에 새로운 쿼리 메서드 `findAllByUserIdAndYmdBetween()`을 추가한다.

    * `year`, `month`, `day` 컬럼을 합산하여 `YYYYMMDD` 정수 형태로 비교하는 방식으로 구현한다.
* `ScheduleService#getSchedulesGroupedByDate()`에서는 Stream 반복 대신 한 번에 조회한 결과를
  `Collectors.groupingBy(s -> s.getDate().toString())`로 그룹화한다.
* `Schedule` 엔티티에 `getDate()` 메서드를 추가하여 `year`, `month`, `day`를 `LocalDate`로 변환한다.

---

## 결과

* **쿼리 횟수**: 기간 일수(N) → **1회로 감소**
* **성능**: 30~90일 단위 조회 시 DB 부하가 크게 감소하고 응답 속도가 개선됨
* **코드 구조**: 조회 로직이 단순화되어 가독성과 유지보수성이 향상됨
* **기능 변화 없음**: 응답 스펙 및 API 동작은 기존과 동일함
* **추가 고려사항**:

    * 대량 데이터 조회 시 메모리 사용량 증가 가능성이 있으므로, 향후 pagination 또는 stream 처리 검토 필요

---

## 대안

| 대안                                  | 설명               | 단점                                 |
| ----------------------------------- | ---------------- | ---------------------------------- |
| (A) 날짜별 쿼리 유지                       | 단순 구현, 코드 변경 최소화 | 기간이 길어질수록 성능 급락, N+1 문제 지속         |
| (B) 기간 단위 단일 쿼리 + 메모리 그룹화 (**선택안**) | 쿼리 1회로 단축, 성능 향상 | 메모리 사용량 증가 가능성                     |
| (C) DB에서 GROUP BY로 날짜별 COUNT까지 처리   | DB 부하 분산 가능      | 복잡한 JPQL/Native 관리 부담, 응답 구조 변경 위험 |

---

## 관련 문서

* PR: `스케줄 조회 시 N+1 쿼리 제거 및 조회 성능 개선`
* Commit: `#119 refactor : 스케줄 조회 시 N+1 쿼리 제거 및 조회 성능 개선`
