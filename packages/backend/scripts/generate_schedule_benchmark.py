#!/usr/bin/env python3
"""Generate deterministic MySQL seed SQL for the monthly schedule benchmark.

The generated SQL is intended for a disposable benchmark database. It only
touches users whose names start with ``benchmark_`` and schedules belonging to
those users.
"""

from __future__ import annotations

import argparse
import datetime as dt
from pathlib import Path


DATASETS = {
    "correctness": (1, 100),
    "baseline": (100, 100),
    "user-heavy": (10, 10_000),
    "global-heavy": (10_000, 100),
}


def sql(value: object) -> str:
    if value is None:
        return "NULL"
    if isinstance(value, bool):
        return "1" if value else "0"
    return "'" + str(value).replace("'", "''") + "'"


def schedule_date(index: int) -> dt.date:
    # 20% outside the requested month, 60% inside it, 20% after it.
    slot = index % 10
    if slot < 2:
        return dt.date(2026, 2, 1) + dt.timedelta(days=(index * 7) % 28)
    if slot < 8:
        return dt.date(2026, 3, 1) + dt.timedelta(days=(index * 17) % 31)
    return dt.date(2026, 4, 1) + dt.timedelta(days=(index * 11) % 30)


def emit_dataset(out, name: str, user_count: int, schedules_per_user: int) -> None:
    out.write(f"-- dataset: {name}, users={user_count}, schedules/user={schedules_per_user}\n")
    prefix = f"benchmark_{name}_"
    for user_index in range(user_count):
        email = f"{prefix}{user_index:05d}@example.test"
        user_name = f"Benchmark {name} {user_index:05d}"
        out.write(
            "INSERT INTO `user` "
            "(fcm_token, user_name, oauth_provider, email, is_deleted, created_at, has_schedule) "
            f"VALUES (NULL, {sql(user_name)}, 'BENCHMARK', {sql(email)}, 0, "
            "'2026-01-01 00:00:00', 1);\n"
        )
        values = []
        for i in range(schedules_per_user):
            date = schedule_date(i)
            checked = (i * 7) % 10 < 6
            schedule_type = "SCHEDULE" if (i * 3) % 10 < 7 else "TODO"
            hour = 1 + (i % 12) if schedule_type == "SCHEDULE" else None
            minute = (i * 5) % 60 if schedule_type == "SCHEDULE" else None
            am_pm = "오전" if hour is not None and i % 2 == 0 else "오후"
            hour24 = hour % 12 if am_pm == "오전" else (hour % 12) + 12
            notification_time = (
                dt.datetime.combine(date, dt.time(hour24, minute))
                - dt.timedelta(minutes=15)
                if hour is not None else None
            )
            notified_at = (
                notification_time.strftime("%Y-%m-%d %H:%M:%S")
                if notification_time is not None else None
            )
            values.append(
                "((SELECT id FROM `user` WHERE email = " + sql(email) + "), "
                + ", ".join([
                    sql(f"{name}-{user_index}-{i}"),
                    str(date.month), str(date.day), sql(date.strftime("%A")),
                    sql(am_pm) if hour is not None else "NULL",
                    str(hour) if hour is not None else "NULL",
                    str(minute) if minute is not None else "NULL",
                    str(date.year), sql(date), "'2026-01-01 00:00:00'",
                    sql(checked), sql(schedule_type), sql(notified_at), "0",
                ]) + ")"
            )
        if values:
            out.write(
                "INSERT INTO schedule "
                "(user_id, title, month, day, day_of_week, am_pm, hour, minute, year, "
                "schedule_date, created_at, checked, type, notified_at, notified) VALUES\n"
                + ",\n".join(values) + ";\n"
            )


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("dataset", choices=sorted(DATASETS))
    parser.add_argument("-o", "--output", type=Path, required=True)
    args = parser.parse_args()

    users, per_user = DATASETS[args.dataset]
    args.output.parent.mkdir(parents=True, exist_ok=True)
    with args.output.open("w", encoding="utf-8") as out:
        out.write("SET NAMES utf8mb4;\nSET FOREIGN_KEY_CHECKS = 0;\n")
        emit_dataset(out, args.dataset, users, per_user)
        out.write("SET FOREIGN_KEY_CHECKS = 1;\n")


if __name__ == "__main__":
    main()
