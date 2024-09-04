package me.ljpb.yosetsukenai.data.room

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.ljpb.yosetsukenai.data.SimplePeriod
import java.time.LocalDate
import java.util.UUID

class TableConverter {
    // SimplePeriod
    @TypeConverter
    fun fromStringToSimplePeriod(string: String): SimplePeriod = SimplePeriod.fromString(string)

    @TypeConverter
    fun fromSimplePeriodToString(period: SimplePeriod): String = period.toString()

    // LocalDate
    @TypeConverter
    fun fromStringToLocalDate(string: String): LocalDate = LocalDate.parse(string)

    @TypeConverter
    fun fromLocalDateToString(localDate: LocalDate): String = localDate.toString()

    // List<String>
    @TypeConverter
    fun fromStringToStringList(string: String): List<String> =
        Json.decodeFromString<List<String>>(string)

    @TypeConverter
    fun fromStringListToString(stringList: List<String>): String = Json.encodeToString(stringList)

    // UUID
    @TypeConverter
    fun fromStringToUuid(string: String): UUID = UUID.fromString(string)

    @TypeConverter
    fun fromUuidToString(uuid: UUID): String = uuid.toString()
}