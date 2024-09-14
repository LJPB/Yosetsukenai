package me.ljpb.yosetsukenai.data.room

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId

class TableConverterTest {
    private val expectPlaces = listOf("aaa", "bbb", "ccc")
    private var converter: TableConverter = TableConverter()
    
    private val expectZoneId = ZoneId.of("Asia/Tokyo")
    private val expectZoneIdString = "Asia/Tokyo"
    
    @Test
    fun fromStringToStringListTest() {
        val string = converter.fromStringListToString(expectPlaces)
        val list = converter.fromStringToStringList(string)
        assertEquals(list, expectPlaces)
    }
    
    @Test
    fun fromZoneIdToStringTest() {
        val string = converter.fromZoneIdToString(expectZoneId)
        assertEquals(expectZoneIdString, string)
    }
    
    @Test
    fun fromStringToZoneIdTest() {
        val zoneId = converter.fromStringToZoneId(expectZoneIdString)
        assertEquals(expectZoneId, zoneId)
    }
}