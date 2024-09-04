package me.ljpb.yosetsukenai.data.room

import org.junit.Assert.assertEquals
import org.junit.Test

class TableConverterTest {
    private val expectPlaces = listOf("aaa", "bbb", "ccc")
    private var converter: TableConverter = TableConverter()
    
    @Test
    fun fromStringToStringListTest() {
        val string = converter.fromStringListToString(expectPlaces)
        val list = converter.fromStringToStringList(string)
        assertEquals(list, expectPlaces)
    }
}