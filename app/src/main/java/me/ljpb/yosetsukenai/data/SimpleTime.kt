package me.ljpb.yosetsukenai.data

/**
 * 時間を表すクラス
 */
class SimpleTime private constructor(hour: Int, minutes: Int) {
    var hour: Int = hour
        private set
    var minutes: Int = minutes
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleTime

        if (hour != other.hour) return false
        if (minutes != other.minutes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minutes
        return result
    }

    override fun toString(): String = "$hour:$minutes"
    
    companion object {
        @Throws(IllegalArgumentException::class)
        fun of(hour: Int, minutes: Int): SimpleTime {
            if (hour >= 24 || hour < 0) {
                throw IllegalArgumentException("Must be 0 <= hour < 24. but hour is $hour")
            }
            if (minutes >= 60 || minutes < 0) {
                throw IllegalArgumentException("Must be 0 <= minutes < 60. but hour is $minutes")
            }
            return SimpleTime(hour, minutes)
        }

        @Throws(IllegalArgumentException::class)
        fun fromString(string: String): SimpleTime {
            val hour: Int
            val minutes: Int
            val tmpList = string.split(":")
            val errorMessage = "Format Error : $string"
            try {
                hour = tmpList[0].toInt()
                minutes = tmpList[1].toInt()
            } catch (message: Exception) {
                throw IllegalArgumentException(errorMessage)
            }
            return SimpleTime(hour, minutes)
        }
    }
}