package me.ljpb.yosetsukenai.data.room

import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.SimplePeriod
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

interface AppDatabaseConverter {
    // SimplePeriod
    fun fromStringToSimplePeriod(string: String): SimplePeriod

    fun fromSimplePeriodToString(period: SimplePeriod): String

    // LocalDate
    fun fromStringToLocalDate(string: String): LocalDate

    fun fromLocalDateToString(localDate: LocalDate): String

    // List<String>
    fun fromStringToStringList(string: String): List<String>

    fun fromStringListToString(stringList: List<String>): String

    // UUID
    fun fromStringToUuid(string: String): UUID

    fun fromUuidToString(uuid: UUID): String

    // ZoneId
    fun fromStringToZoneId(string: String): ZoneId = ZoneId.of(string)

    fun fromZoneIdToString(zoneId: ZoneId): String = zoneId.id

    // PeriodAndTime
    fun fromStringToPeriodAndTime(string: String): PeriodAndTime

    fun fromPeriodAndTimeToString(periodAndTime: PeriodAndTime): String
}