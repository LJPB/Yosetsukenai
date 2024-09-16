package me.ljpb.yosetsukenai.ui

import android.content.Context
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun getTextOfSimplePeriod(simplePeriod: SimplePeriod, context: Context): String =
    when (simplePeriod.periodUnit) {
        PeriodUnit.Day -> context.getString(R.string.validity_period_day, simplePeriod.number)
        PeriodUnit.Week -> context.getString(R.string.validity_period_week, simplePeriod.number)
        PeriodUnit.Month -> context.getString(R.string.validity_period_month, simplePeriod.number)
        PeriodUnit.Year -> context.getString(R.string.validity_period_year, simplePeriod.number)
    }

fun getTextOfLocalDate(localDate: LocalDate, context: Context): String =
    context.getString(
        R.string.formated_date,
        localDate.year,
        localDate.monthValue,
        localDate.dayOfMonth,
    )

fun getTextOfNotificationEntity(notification: NotificationEntity, context: Context): String = getNotificationText(notification.schedule, notification.time, context)

fun getNotificationText(simplePeriod: SimplePeriod, simpleTime: SimpleTime, context: Context): String {
    val timeText = getTextOfSimpleTime(simpleTime, context)
    if (simplePeriod.number == 0 && simplePeriod.periodUnit == PeriodUnit.Day) { // 当日
        return context.getString(
            R.string.notification_text_on_the_day,
            timeText
        )
    } else {
        return when (simplePeriod.periodUnit) {
            PeriodUnit.Day -> context.getString(
                R.string.notification_text_before_day,
                simplePeriod.number,
                timeText
            )

            PeriodUnit.Week -> context.getString(
                R.string.notification_text_before_week,
                simplePeriod.number,
                timeText
            )

            PeriodUnit.Month -> context.getString(
                R.string.notification_text_before_month,
                simplePeriod.number,
                timeText
            )

            PeriodUnit.Year -> context.getString(
                R.string.notification_text_before_year,
                simplePeriod.number,
                timeText
            )
        }
    }
}

fun getTextOfSimpleTime(simpleTime: SimpleTime, context: Context): String =
    context.getString(R.string.simple_time_text, simpleTime.hour, simpleTime.minutes)

fun localDateToEpochSecond(localDate: LocalDate, zoneId: ZoneId): Long {
    val zonedDateTime = ZonedDateTime.of(
        LocalDateTime.of(localDate, LocalTime.of(12, 0)),
        zoneId
    )
    return zonedDateTime.toEpochSecond()
}

fun epochSecondToLocalDate(epochSecond: Long, zoneId: ZoneId): LocalDate {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(epochSecond),
        zoneId
    )
    return localDateTime.toLocalDate()
}