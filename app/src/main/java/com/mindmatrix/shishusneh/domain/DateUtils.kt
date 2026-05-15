package com.mindmatrix.shishusneh.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val zone: ZoneId = ZoneId.systemDefault()
private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

fun LocalDate.toMillis(): Long = atStartOfDay(zone).toInstant().toEpochMilli()

fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(zone).toLocalDate()

fun formatDate(millis: Long): String = millis.toLocalDate().format(formatter)

fun ageInWeeks(dateOfBirth: Long, today: LocalDate = LocalDate.now()): Int =
    ChronoUnit.WEEKS.between(dateOfBirth.toLocalDate(), today).toInt().coerceAtLeast(0)

fun ageLabel(dateOfBirth: Long): String {
    val weeks = ageInWeeks(dateOfBirth)
    return if (weeks < 8) "$weeks weeks old" else "${weeks / 4} months old"
}
