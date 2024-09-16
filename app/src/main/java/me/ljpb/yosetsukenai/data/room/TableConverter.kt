package me.ljpb.yosetsukenai.data.room

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class TableConverter : AppDatabaseConverter {
    // SimplePeriod
    @TypeConverter
    override fun fromStringToSimplePeriod(string: String): SimplePeriod =
        SimplePeriod.fromString(string)

    @TypeConverter
    override fun fromSimplePeriodToString(period: SimplePeriod): String = period.toString()

    // LocalDate
    @TypeConverter
    override fun fromStringToLocalDate(string: String): LocalDate = LocalDate.parse(string)

    @TypeConverter
    override fun fromLocalDateToString(localDate: LocalDate): String = localDate.toString()

    // List<String>
    @TypeConverter
    override fun fromStringToStringList(string: String): List<String> =
        Json.decodeFromString<List<String>>(string)

    @TypeConverter
    override fun fromStringListToString(stringList: List<String>): String =
        Json.encodeToString(stringList)

    // UUID
    @TypeConverter
    override fun fromStringToUuid(string: String): UUID = UUID.fromString(string)

    @TypeConverter
    override fun fromUuidToString(uuid: UUID): String = uuid.toString()
    
    // ZoneId
    @TypeConverter
    override fun fromStringToZoneId(string: String): ZoneId = ZoneId.of(string)

    @TypeConverter
    override fun fromZoneIdToString(zoneId: ZoneId): String = zoneId.id

    // PeriodAndTime
    @TypeConverter
    override fun fromStringToPeriodAndTime(string: String): PeriodAndTime {
        val list = Json.decodeFromString<List<String>>(string)
        return PeriodAndTime(
            SimplePeriod.fromString(list[0]),
            SimpleTime.fromString(list[1]),
        )
    }

    @TypeConverter
    override fun fromPeriodAndTimeToString(periodAndTime: PeriodAndTime): String {
        val periodString = periodAndTime.period.toString()
        val timeString = periodAndTime.time.toString()
        return Json.encodeToString(listOf(periodString, timeString))
    }
}