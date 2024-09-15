package me.ljpb.yosetsukenai.ui.components.edit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate
import java.time.ZoneId

data class PeriodAndTime(
    val period: SimplePeriod, val time: SimpleTime
)

class RepellentEditViewModel(
    private val repellent: RepellentScheduleEntity?,
    private val notifies: List<NotifyEntity>
) : ViewModel() {
    private val _name = MutableStateFlow(repellent?.name ?: "")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _startDate = MutableStateFlow(repellent?.startDate ?: LocalDate.now())
    val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

    private val _validityPeriodUnit =
        MutableStateFlow(repellent?.validityPeriod?.periodUnit ?: PeriodUnit.Day)
    val validityPeriodUnit: StateFlow<PeriodUnit> = _validityPeriodUnit.asStateFlow()

    private val _validityNumber = MutableStateFlow(repellent?.validityPeriod?.number ?: 30)
    val validityNumber: StateFlow<Int> = _validityNumber.asStateFlow()

    private val _zoneId = MutableStateFlow(repellent?.zoneId ?: ZoneId.systemDefault())
    val zoneId: StateFlow<ZoneId> = _zoneId.asStateFlow()

    private val _places = MutableStateFlow<List<String>>(repellent?.places ?: emptyList())
    val places: StateFlow<List<String>> = _places.asStateFlow()

    private val _notifyList = MutableStateFlow<List<PeriodAndTime>>(
        mutableListOf<PeriodAndTime>()
            .apply {
                addAll(notifies.map { notify -> PeriodAndTime(notify.schedule, notify.time) })
            }
            .toList()
    )

    val notifyList: StateFlow<List<PeriodAndTime>> = _notifyList.asStateFlow()

    fun setName(string: String) {
        _name.update { string }
    }

    fun setStartDate(date: LocalDate) {
        _startDate.update { date }
    }

    fun setValidityPeriodUnit(periodUnit: PeriodUnit) {
        _validityPeriodUnit.update { periodUnit }
    }

    fun setValidityNumber(number: Int) {
        _validityNumber.update { number }
    }

    fun addPlace(string: String) {
        _places.update { it.addedList(string, false) }
    }

    fun removePlace(index: Int, string: String) {
        _places.update { it.removedList(index, string) }
    }

    fun addNotify(periodAndTime: PeriodAndTime) {
        _notifyList.update { it.addedList(periodAndTime, false) }
    }
    
    fun removeNotify(index: Int, periodAndTime: PeriodAndTime) {
        _notifyList.update { it.removedList(index, periodAndTime) }
    }
}

/**
 * @param overlap (追加後の)要素の重複を認めるかどうか
 */
private fun <T> List<T>.addedList(item: T, overlap: Boolean = true): List<T> {
    return if (!overlap && item in this) this else (this + listOf(item))
}

private fun <T> List<T>.removedList(index: Int, item: T): List<T> {
    val original = this
    val tmp = mutableListOf<T>().apply { addAll(original) }
    return tmp.xRemove(index, item)
}

private fun <T> MutableList<T>.xRemove(index: Int, item: T): MutableList<T> {
    // 完全に同時に削除(タップ)したのインデックスのズレ対策にtrt-catchで囲んでいる
    // 例えば[a, b]を同時に削除した時，aが先に削除されたらその時点でのリストは[b]となるが，
    // 削除リクエストとしてindex = 1となっていたらIndexOutOfBoundsExceptionとなる
    // また[a, b, c, d]でb, cを同時に削除した時，
    // index = 1(b)を削除して，index = 2(c)を削除するという順番で処理が行われた時
    // bが削除された時点でのリストが[a, c, d]となり，この場合のindex = 2はdとなり
    // 本来は削除しない要素が削除されてしまう
    // その対策として，list[index] == item という条件を加えている
    try {
        if (this[index] == item) {
            this.removeAt(index)
        }
    } catch (_: IndexOutOfBoundsException) {
    }
    return this
}