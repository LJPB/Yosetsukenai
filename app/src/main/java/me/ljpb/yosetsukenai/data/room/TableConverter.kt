package me.ljpb.yosetsukenai.data.room

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.ljpb.yosetsukenai.data.SimplePeriod
import java.time.LocalDate

class TableConverter {
    @TypeConverter
    fun fromStringToSimplePeriod(string: String): SimplePeriod = SimplePeriod.fromString(string)

    @TypeConverter
    fun fromSimplePeriodToString(period: SimplePeriod): String = period.toString()

    @TypeConverter
    fun fromStringToLocalDate(string: String): LocalDate = LocalDate.parse(string)

    @TypeConverter
    fun fromLocalDateToString(localDate: LocalDate): String = localDate.toString()

    @TypeConverter
    fun fromStringToStringList(string: String): List<String> =
        Json.decodeFromString<List<String>>(string)

    @TypeConverter
    fun fromStringListToString(stringList: List<String>): String = Json.encodeToString(stringList)
}