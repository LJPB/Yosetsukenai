package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.ljpb.yosetsukenai.data.NotifyAction
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate
import java.time.ZoneId

data class PeriodAndTime(val period: SimplePeriod, val time: SimpleTime)

class RepellentEditViewModel(
    private val repellent: RepellentScheduleEntity?,
    private val notifications: List<NotifyEntity>,
    private val repellentAction: RepellentScheduleAction,
    private val notifyAction: NotifyAction,
) : ViewModel() {
    companion object {
        const val EMPTY_INT = -1
    }

    // RepellentScheduleEntityの新規追加か更新かのフラグ
    val isUpdate = repellent != null

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

    private val _notifyList = MutableStateFlow(
        mutableListOf<PeriodAndTime>()
            .apply {
                addAll(notifications.map { notify -> PeriodAndTime(notify.schedule, notify.time) })
            }
            .toList()
    )
    val notifyList: StateFlow<List<PeriodAndTime>> = _notifyList.asStateFlow()

    // 保存ボタンを押した時に更新するために，削除した通知情報の保持
    private val deletedNotifyList: MutableList<PeriodAndTime> = mutableListOf()

    // 保存ボタンを押した時に更新するために，追加した通知情報の保持
    private val addedNotifyList: MutableList<PeriodAndTime> = mutableListOf()

    // 保存可能かどうかのフラグ
    val canSave: StateFlow<Boolean> = combine(name, validityNumber) { nameValue, numberValue ->
        nameValue.isNotEmpty() && numberValue != EMPTY_INT
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        repellent?.name?.isNotEmpty() ?: false
    )

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
        _places.update { it.addedList(string) }
    }

    fun removePlace(string: String) {
        _places.update { it.removedList(string) }
    }

    fun addNotify(periodAndTime: PeriodAndTime) {
        _notifyList.update { list ->
            val tmp = mutableListOf<PeriodAndTime>().apply { addAll(list) }
            if (periodAndTime !in tmp) { // 通知時間の重複は許容しない
                addedNotifyList.takeIf { periodAndTime !in it }?.add(periodAndTime) // addedNotifyListに含まれていなければ追加する
                tmp.add(periodAndTime)
            }
            tmp
        }
    }

    fun removeNotify(periodAndTime: PeriodAndTime) {
        _notifyList.update { list ->
            val tmp = mutableListOf<PeriodAndTime>().apply { addAll(list) }
            addedNotifyList.remove(periodAndTime)
            deletedNotifyList.takeIf { periodAndTime !in it }?.add(periodAndTime) // deletedNotifyListに含まれていなければ追加する
            tmp.remove(periodAndTime)
            tmp
        }
    }

    fun saveRepellent() {
        /*******************************  注意  *******************************
         * deletedNotifyListで削除 → addedNotifyListで追加 の順番で処理する
         * UI操作で「通知を削除 → PeriodAndTimeが同じ通知を追加」としたとき
         * 通知の追加処理を先に行うと，その時点で通知日時が同じ通知が2件(以上)となる
         * 削除処理を先に行うことで，本来削除したい通知－つまり先に存在している通知を削除して
         * それから新しい通知を追加する，というUI操作と同じ順番で処理ができる
         *********************************************************************/
        // deleteNotification()
        // add/updateRepellent()
        // addNotification(repellent)
    }

    fun deleteRepellent() {

    }
}

/**
 * 重複を許さない要素の追加
 */
private fun <T> List<T>.addedList(item: T): List<T> {
    return if (item in this) this else (this + listOf(item))
}

private fun <T> List<T>.removedList(item: T): List<T> {
    val original = this
    val tmp = mutableListOf<T>().apply { addAll(original) }
    return tmp.xRemove(item)
}

private fun <T> MutableList<T>.xRemove(item: T): MutableList<T> {
    this.remove(item)
    return this
}

private fun NotifyEntity.match(periodAndTime: PeriodAndTime): Boolean =
    this.schedule == periodAndTime.period && this.time == periodAndTime.time

/**
 * 渡されたPeriodAndTimeと同じSimplePeriod, SimpleTimeを持つNotifyEntityのインデックス値を返す
 * 存在しない場合は-1が返される
 */
private fun List<NotifyEntity>.indexOf(periodAndTime: PeriodAndTime): Int =
    this.indexOfFirst { it.match(periodAndTime) }