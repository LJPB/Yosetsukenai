package me.ljpb.yosetsukenai.ui

import android.content.Context
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
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