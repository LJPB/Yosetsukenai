package me.ljpb.yosetsukenai.ui

import android.content.Context
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import java.time.LocalDate

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

fun getTextOfNotify(notify: NotifyEntity, context: Context): String {
    val schedule = notify.schedule
    val time = notify.time
    val timeText = getTextOfSimpleTime(time, context)
    if (schedule.number == 0 && schedule.periodUnit == PeriodUnit.Day) { // 当日
        return context.getString(
            R.string.notify_text_on_the_day,
            timeText
        )
    } else {
        return when (schedule.periodUnit) {
            PeriodUnit.Day -> context.getString(
                R.string.notify_text_before_day,
                schedule.number,
                timeText
            )

            PeriodUnit.Week -> context.getString(
                R.string.notify_text_before_week,
                schedule.number,
                timeText
            )

            PeriodUnit.Month -> context.getString(
                R.string.notify_text_before_month,
                schedule.number,
                timeText
            )

            PeriodUnit.Year -> context.getString(
                R.string.notify_text_before_year,
                schedule.number,
                timeText
            )
        }
    }
}

fun getTextOfSimpleTime(simpleTime: SimpleTime, context: Context): String =
    context.getString(R.string.simple_time_text, simpleTime.hour, simpleTime.minutes)