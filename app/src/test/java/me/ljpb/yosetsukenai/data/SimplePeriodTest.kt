package me.ljpb.yosetsukenai.data

import me.ljpb.yosetsukenai.data.SimplePeriod.Companion.SEPARATE_CHARACTER
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SimplePeriodTest {
    private val expectNumber = 100
    private val expectPeriodUnit = PeriodUnit.Day
    private val expectPeriod = SimplePeriod.of(expectNumber, expectPeriodUnit)
    private val expectString = "$expectNumber$SEPARATE_CHARACTER${expectPeriodUnit.name}"

    @Test
    fun toStringTest() {
        val string = expectPeriod.toString()
        assertEquals(expectString, string)
    }

    @Test
    fun fromStringTest() {
        val period = SimplePeriod.fromString(expectString)
        assertEquals(expectPeriod, period)
    }

    // periodUnitがない場合
    @Test
    fun fromStringTestThrowsExceptionOne() {
        val string = "$expectNumber$SEPARATE_CHARACTER"
        fromStringTestThrows<IllegalArgumentException>(string)
    }

    // numberが数字じゃない場合
    @Test
    fun fromStringTestThrowsExceptionTwo() {
        val string = "a$SEPARATE_CHARACTER${expectPeriodUnit.name}"
        fromStringTestThrows<IllegalArgumentException>(string)
    }

    // periodTypeが異なる場合
    @Test
    fun fromStringTestThrowsExceptionThree() {
        val string = "$expectNumber${SEPARATE_CHARACTER}a"
        fromStringTestThrows<IllegalArgumentException>(string)
    }

    // numberがない場合
    @Test
    fun fromStringTestThrowsExceptionFour() {
        val string = "$SEPARATE_CHARACTER${expectPeriodUnit.name}"
        fromStringTestThrows<IllegalArgumentException>(string)
    }

    // SEPARATE_CHARACTERが異なる場合
    @Test
    fun fromStringTestThrowsExceptionFive() {
        val string = "${expectNumber}t${expectPeriodUnit.name}"
        fromStringTestThrows<IllegalArgumentException>(string)
    }

    // 空の場合
    @Test
    fun fromStringTestThrowsExceptionSix() {
        val string = ""
        fromStringTestThrows<IllegalArgumentException>(string)
    }

    private inline fun <reified T : Throwable> fromStringTestThrows(string: String) {
        val expectMessage = getErrorMessage(string)
        val exception = assertThrows(T::class.java) {
            SimplePeriod.fromString(string)
        }
        assertEquals(exception.message, expectMessage)
    }

    private fun getErrorMessage(string: String) = "Format Error : $string"

}