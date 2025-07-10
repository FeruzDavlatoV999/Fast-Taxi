package uz.mobile.taxi.data.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatDate(dateString: String): String {
    val instant = Instant.parse(dateString)
    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
    return "${localDateTime.dayOfMonth.toString().padStart(2, '0')}.${localDateTime.monthNumber.toString().padStart(2, '0')}.${localDateTime.year}"
}


fun formatTime(dateString: String): String {
    val instant = Instant.parse(dateString)
    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
    return "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
}
