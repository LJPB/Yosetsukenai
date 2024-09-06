package me.ljpb.yosetsukenai.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SimpleTimeTest {
    private val expectHour = 12
    private val expectMinutes = 34
    private val expectTime = SimpleTime.of(expectHour, expectMinutes)
    private val expectString = "$expectHour:$expectMinutes"

    @Test
    fun toStringTest() {
        val string = expectTime.toString()
        assertEquals(expectString, string)
    }

    @Test
    fun fromStringTestOne() {
        val time = SimpleTime.fromString(expectString)
        assertEquals(expectTime, time)
    }

    @Test
    fun fromStringTestTwo() {
        val time = SimpleTime.fromString("01:01")
        assertEquals(SimpleTime.of(1, 1), time)
    }

    // 数字(hour)がない
    @Test
    fun fromStringTestErrorOne() {
        val str = ":2"
        fromStringTestThrows(str)
    }

    // 数字(minutes)がない
    @Test
    fun fromStringTestErrorTwo() {
        val str = "12:"
        fromStringTestThrows(str)
    }

    // 数字(hour)が非数字文字
    @Test
    fun fromStringTestErrorThree() {
        val str = "a:2"
        fromStringTestThrows(str)
    }

    // 区切り文字がない
    @Test
    fun fromStringTestErrorFour() {
        val str = "22"
        fromStringTestThrows(str)
    }

    // 区切り文字が違う
    @Test
    fun fromStringTestErrorFive() {
        val str = "1a2"
        fromStringTestThrows(str)
    }

    // hourが範囲外
    @Test
    fun ofTestErrorOne() {
        val hour = -1
        ofTestThrows(
            hour = hour,
            minutes = 1,
            expectMessage = "Must be 0 <= hour < 24. but hour is $hour"
        )
    }

    // minutesが範囲外
    @Test
    fun ofTestErrorTwo() {
        val minutes = 60
        ofTestThrows(
            hour = 0,
            minutes = minutes,
            expectMessage = "Must be 0 <= minutes < 60. but hour is $minutes"
        )
    }

    private fun fromStringTestThrows(string: String) {
        val expectMessage = geFromStringErrorMessage(string)
        val exception = assertThrows(IllegalArgumentException::class.java) {
            SimpleTime.fromString(string)
        }
        assertEquals(exception.message, expectMessage)
    }

    private fun geFromStringErrorMessage(str: String) = "Format Error : $str"

    private fun ofTestThrows(hour: Int, minutes: Int, expectMessage: String) {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            SimpleTime.of(hour, minutes)
        }
        assertEquals(exception.message, expectMessage)
    }

}