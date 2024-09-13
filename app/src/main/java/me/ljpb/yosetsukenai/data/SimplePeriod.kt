package me.ljpb.yosetsukenai.data

/**
 * 期間の単位
 */
enum class PeriodUnit {
    Day,
    Week,
    Month,
    Year
}

/**
 * 期間を表すクラス
 * @param number 日数や週数など期間の長さ
 * @param periodUnit 日や週など期間の単位
 */
class SimplePeriod private constructor(var number: Int, var periodUnit: PeriodUnit) {
    override fun toString(): String {
        return "$number$SEPARATE_CHARACTER${periodUnit.name}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimplePeriod

        if (number != other.number) return false
        if (periodUnit != other.periodUnit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + periodUnit.hashCode()
        return result
    }
    
    fun copy(number: Int = this.number, periodUnit: PeriodUnit = this.periodUnit): SimplePeriod = of(number, periodUnit)

    companion object {
        const val SEPARATE_CHARACTER = "@"

        fun of(number: Int, periodUnit: PeriodUnit): SimplePeriod = SimplePeriod(number, periodUnit)

        fun ofDays(days: Int): SimplePeriod = of(days, PeriodUnit.Day)

        fun ofWeeks(weeks: Int): SimplePeriod = of(weeks, PeriodUnit.Week)

        fun ofMonths(months: Int): SimplePeriod = of(months, PeriodUnit.Month)

        fun ofYears(years: Int): SimplePeriod = of(years, PeriodUnit.Year)

        /**
         * 与えられた文字列からPeriodオブジェクトを作る
         * @param string 数字@PeriodType.name が正しい形式 (@はSEPARATE_CHARACTER)
         * @throws IllegalArgumentException 不正な形式の文字列が渡されてPeriodオブジェクトを作れない場合に発生する
         * エラーメッセージの形式：Format Error : $string
         */
        @Throws(IllegalArgumentException::class)
        fun fromString(string: String): SimplePeriod {
            val number: Int
            val periodUnit: PeriodUnit
            val tmpList = string.split(SEPARATE_CHARACTER)
            val errorMessage = "Format Error : $string"
            try {
                number = tmpList[0].toInt()
                periodUnit = PeriodUnit.valueOf(tmpList[1])
            } catch (message: Exception) {
                throw IllegalArgumentException(errorMessage)
            }
            return SimplePeriod(number, periodUnit)
        }
    }
}