package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ljpb.yosetsukenai.data.NotificationAction
import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate
import java.time.ZoneId
import java.util.Collections.addAll

class RepellentEditViewModel(
    private val repellent: RepellentScheduleEntity?,
    private val notifications: List<NotificationEntity>,
    private val repellentAction: RepellentScheduleAction,
    private val notifyAction: NotificationAction,
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

    private val _notificationList = MutableStateFlow(
        mutableListOf<PeriodAndTime>()
            .apply {
                addAll(notifications.map { notification -> PeriodAndTime(notification.schedule, notification.time) })
            }
            .toList()
    )
    val notificationList: StateFlow<List<PeriodAndTime>> = _notificationList.asStateFlow()

    // 保存ボタンを押した時に更新するために，削除した通知情報の保持
    private val deletedNotifyList: MutableList<PeriodAndTime> = mutableListOf()

    // 保存ボタンを押した時に更新するために，追加した通知情報の保持
    private val addedNotifyList: MutableList<PeriodAndTime> = mutableListOf()

    // 保存可能かどうかのフラグ
    val canSave: StateFlow<Boolean> = combine(name, validityNumber) { nameValue, numberValue ->
        nameValue.isNotEmpty() && numberValue != EMPTY_INT
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
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

    fun addNotification(periodAndTime: PeriodAndTime) {
        _notificationList.update { list ->
            val tmp = mutableListOf<PeriodAndTime>().apply { addAll(list) }
            if (periodAndTime !in tmp) { // 通知時間の重複は許容しない
                addedNotifyList.takeIf { periodAndTime !in it }
                    ?.add(periodAndTime) // addedNotifyListに含まれていなければ追加する
                tmp.add(periodAndTime)
            }
            tmp
        }
    }

    fun removeNotification(periodAndTime: PeriodAndTime) {
        _notificationList.update { list ->
            val tmp = mutableListOf<PeriodAndTime>().apply { addAll(list) }
            addedNotifyList.remove(periodAndTime)
            deletedNotifyList.takeIf { periodAndTime !in it }
                ?.add(periodAndTime) // deletedNotifyListに含まれていなければ追加する
            tmp.remove(periodAndTime)
            tmp
        }
    }

    fun saveRepellent() = viewModelScope.launch {
        /*******************************  注意  *******************************
         * deletedNotifyListで削除 → addedNotifyListで追加 の順番で処理する
         * UI操作で「通知を削除 → PeriodAndTimeが同じ通知を追加」としたとき
         * 通知の追加処理を先に行うと，その時点で通知日時が同じ通知が2件(以上)となる
         * 削除処理を先に行うことで，本来削除したい通知－つまり先に存在している通知を削除して
         * それから新しい通知を追加する，というUI操作と同じ順番で処理ができる
         *********************************************************************/
        // add/updateRepellent() Notification用にIDが必要だから一番最初にする
        // deleteNotification()
        // addNotification(repellent)

        val repellentId = if (isUpdate) updateRepellent() else addRepellent()
        // TODO: 通知関連の処理 
    }

    fun deleteRepellent() = viewModelScope.launch {
        if (repellent != null) repellentAction.delete(repellent)
        notifications.forEach { notifyAction.delete(it) }
    }

    private suspend fun addRepellent(): Long {
        val deferred = viewModelScope.async {
            repellentAction.insert(getRepellent())
        }.await()
        return deferred
    }

    private suspend fun updateRepellent(): Long {
        if (repellent == null) return -1
        viewModelScope.launch {
            repellentAction.update(getRepellent())
        }
        return repellent.id
    }

    private fun getRepellent(): RepellentScheduleEntity {
        val entityName = name.value
        val entityPeriod = SimplePeriod.of(validityNumber.value, validityPeriodUnit.value)
        val entityStartDate = startDate.value
        val entityFinishDate = entityStartDate.addPeriod(entityPeriod)
        val entityPlaces = places.value
        val entityIgnore = false
        val entityZoneId = zoneId.value

        val tmp = RepellentScheduleEntity(
            name = entityName,
            validityPeriod = entityPeriod,
            startDate = entityStartDate,
            finishDate = entityFinishDate,
            places = entityPlaces,
            ignore = entityIgnore,
            zoneId = entityZoneId
        )
        val entity = repellent?.updateValue(tmp) ?: tmp
        return entity
    }
}

/**
 * id以外を渡されたオブジェクトの値に更新したRepellentScheduleEntityを返す
 */
private fun RepellentScheduleEntity.updateValue(repellent: RepellentScheduleEntity): RepellentScheduleEntity =
    this.copy(
        name = repellent.name,
        validityPeriod = repellent.validityPeriod,
        startDate = repellent.startDate,
        finishDate = repellent.finishDate,
        places = repellent.places,
        ignore = repellent.ignore,
        zoneId = repellent.zoneId
    )

/**
 * 渡された期間を追加したLocalDateを返す
 */
private fun LocalDate.addPeriod(simplePeriod: SimplePeriod): LocalDate =
    when (simplePeriod.periodUnit) {
        PeriodUnit.Day -> this.plusDays(simplePeriod.number.toLong())
        PeriodUnit.Week -> this.plusWeeks(simplePeriod.number.toLong())
        PeriodUnit.Month -> this.plusMonths(simplePeriod.number.toLong())
        PeriodUnit.Year -> this.plusYears(simplePeriod.number.toLong())
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

private fun NotificationEntity.match(periodAndTime: PeriodAndTime): Boolean =
    this.schedule == periodAndTime.period && this.time == periodAndTime.time

/**
 * 渡されたPeriodAndTimeと同じSimplePeriod, SimpleTimeを持つNotifyEntityのインデックス値を返す
 * 存在しない場合は-1が返される
 */
private fun List<NotificationEntity>.indexOf(periodAndTime: PeriodAndTime): Int =
    this.indexOfFirst { it.match(periodAndTime) }