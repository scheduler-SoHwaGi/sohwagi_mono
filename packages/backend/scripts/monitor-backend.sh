#!/usr/bin/env bash
set -euo pipefail

# Monitor a locally running Spring Boot process during a load test.
# Usage: ./monitor-backend.sh [pid] [interval_seconds] [output_csv]

PID="${1:-}"
INTERVAL="${2:-1}"
OUTPUT="${3:-backend-metrics-$(date +%Y%m%d-%H%M%S).csv}"

if [[ -z "$PID" ]]; then
  PID="$(pgrep -f 'sohwagi-app|org\.project\.sohwagi\.SohwagiApplication' | head -n 1 || true)"
fi

if [[ -z "$PID" ]] || ! kill -0 "$PID" 2>/dev/null; then
  echo "Backend Java process was not found. Pass its PID as the first argument." >&2
  exit 1
fi

echo "timestamp,pid,cpu_percent,rss_kb,threads,heap_used_kb,young_gc_count,full_gc_count,gc_time_seconds" > "$OUTPUT"
echo "Monitoring PID $PID every ${INTERVAL}s. Writing $OUTPUT"

while kill -0 "$PID" 2>/dev/null; do
  timestamp="$(date '+%Y-%m-%dT%H:%M:%S%z')"
  process_stats="$(ps -p "$PID" -o '%cpu=' -o 'rss=' -o 'thcount=' 2>/dev/null \
    || ps -p "$PID" -o '%cpu=' -o 'rss=' -o 'nlwp=')"
  process_stats="$(printf '%s' "$process_stats" | awk 'NR == 1 {print $1 "," $2 "," $3}')"
  cpu_percent="$(printf '%s' "$process_stats" | cut -d, -f1)"
  rss_kb="$(printf '%s' "$process_stats" | cut -d, -f2)"
  threads="$(printf '%s' "$process_stats" | cut -d, -f3)"

  heap_used_kb=""
  young_gc_count=""
  full_gc_count=""
  gc_time_seconds=""
  if command -v jstat >/dev/null 2>&1; then
    jstat_line="$(jstat -gc "$PID" 2>/dev/null | tail -n 1 || true)"
    if [[ -n "$jstat_line" ]]; then
      read -r heap_used_kb young_gc_count full_gc_count gc_time_seconds < <(
        printf '%s\n' "$jstat_line" | awk '{
          printf "%.0f %.0f %.0f %.3f", $3+$4+$6+$8+$10+$12, $13, $15, $14+$16
        }'
      )
    fi
  fi

  echo "$timestamp,$PID,$cpu_percent,$rss_kb,$threads,$heap_used_kb,$young_gc_count,$full_gc_count,$gc_time_seconds" >> "$OUTPUT"
  sleep "$INTERVAL"
done

echo "Backend process $PID stopped. Metrics saved to $OUTPUT"
