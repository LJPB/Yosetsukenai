package me.ljpb.yosetsukenai.data.room

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import java.time.LocalDate
import java.util.UUID

class TableConverter : AppDatabaseConverter {
    // SimplePeriod
    @TypeConverter
    override fun fromStringToSimplePeriod(string: String): SimplePeriod = SimplePeriod.fromString(string)

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
    override fun fromStringListToString(stringList: List<String>): String = Json.encodeToString(stringList)

    // UUID
    @TypeConverter
    override fun fromStringToUuid(string: String): UUID = UUID.fromString(string)

    @TypeConverter
    override fun fromUuidToString(uuid: UUID): String = uuid.toString()
    
    // SimpleTime
    @TypeConverter
    override fun fromStringToSimpleTime(string: String): SimpleTime = SimpleTime.fromString(string)
    
    @TypeConverter
    override fun fromSimpleTimeToString(time: SimpleTime): String = time.toString()
}