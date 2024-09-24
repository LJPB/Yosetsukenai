package me.ljpb.yosetsukenai.ui.screens

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
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.notification.AppNotificationManager
import java.time.LocalDate
import java.time.ZoneId

/**
 * 通知を追加時の状態
 */
enum class NotificationState {
    /** 追加に成功 */
    Success,

    /** すでに存在する */
    Exist,

    /** 開始日以前 */
    BeforeStartDate
}

class RepellentEditViewModel(
    private val repellent: RepellentScheduleEntity?,
    private val existingNotifications: List<NotificationEntity>,
    private val repellentAction: RepellentScheduleAction,
    private val notificationAction: NotificationAction,
) : ViewModel() {
    companion object {
        /**
         * 未入力を表すInt値
         */
        const val EMPTY_INT = -1
    }

    // RepellentScheduleEntityの新規追加か更新かのフラグ
    val isUpdate = repellent != null

    // 虫除けの名前
    private val _name = MutableStateFlow(repellent?.name ?: "")
    val name: StateFlow<String> = _name.asStateFlow()

    // 虫除けの開始日
    private val _startDate = MutableStateFlow(repellent?.startDate ?: LocalDate.now())
    val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

    // 虫除けの有効期間の単位
    private val _validityPeriodUnit =
        MutableStateFlow(repellent?.validityPeriod?.periodUnit ?: PeriodUnit.Day)
    val validityPeriodUnit: StateFlow<PeriodUnit> = _validityPeriodUnit.asStateFlow()

    // 虫除けの有効期間の日数部分
    private val _validityNumber = MutableStateFlow(repellent?.validityPeriod?.number ?: 30)
    val validityNumber: StateFlow<Int> = _validityNumber.asStateFlow()

    // 登録時のタイムゾーン
    private val _zoneId = MutableStateFlow(repellent?.zoneId ?: ZoneId.systemDefault())
    val zoneId: StateFlow<ZoneId> = _zoneId.asStateFlow()

    // 虫除けを使用した場所
    private val _places = MutableStateFlow<List<String>>(repellent?.places ?: emptyList())
    val places: StateFlow<List<String>> = _places.asStateFlow()

    // 既存の通知リスト
    private val _existingNotificationList = MutableStateFlow(existingNotifications)

    val existingNotificationList = _existingNotificationList.asStateFlow()

    // 削除された既存の通知のリスト
    private val deletedExistingNotificationList = mutableListOf<NotificationEntity>()

    // 新規追加した通知のリスト
    private val _newNotificationList = MutableStateFlow(listOf<PeriodAndTime>())

    val newNotificationList = _newNotificationList.asStateFlow()

    // 新規追加したものの削除された通知のリスト
    private val deletedNotificationList: MutableList<PeriodAndTime> = mutableListOf()

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
        if (number == 0) return
        _validityNumber.update { number }
    }

    /**
     * 場所をリストに追加する
     * すでに存在する場所は追加できない
     * @return 追加できたらtrue, 追加できなかったらfalse
     */
    fun addPlace(string: String): Boolean {
        if (string in places.value) return false
        _places.update { it.addedList(string) }
        return true
    }

    /**
     * 場所をリストから削除する
     */
    fun removePlace(string: String) {
        _places.update { it.removedList(string) }
    }

    /**
     * UI操作による通知の追加
     * 既存/新規追加済みの場合は重複して追加しない
     */
    fun addNotification(periodAndTime: PeriodAndTime): NotificationState {
        // TODO: 開始日以前かをチェックする 
        // 既存の通知リスト(existingNotificationList)に同じ時間の通知があればtrue，なければfalse
        val isExist = existingNotificationList.value
            .find { it.match(periodAndTime) }
            // 同じ時間の通知が存在する場合，findの戻り値がnullではないから，.runが実行されてtrueが返される
            // 同じ時間の通知が存在しない場合，findの戻り値がnullとなり，エルビス演算子でfalseが返される
            ?.run { true } ?: false
        if (isExist) return NotificationState.Exist // 存在する場合は，重複して追加できない

        /*********** 追加する通知の日時が開始日以前に設定されているかをチェックする処理 ***********/
        // このメソッドを呼んだ時点で設定されてある有効期間
        val tmpValidity = SimplePeriod.of(validityNumber.value, validityPeriodUnit.value)
        // 虫除けの終了日(通知がなる日を求めるためのもの)
        val tmpFinishDate = startDate.value.addPeriod(
            SimplePeriod.of(
                validityNumber.value,
                validityPeriodUnit.value
            )
        )
        // 通知がなる日
        val pushDate = tmpFinishDate.minusPeriod(periodAndTime.period)

        if (pushDate.isBefore(startDate.value)) return NotificationState.BeforeStartDate

        val isContain = periodAndTime in newNotificationList.value
        if (isContain) return NotificationState.Exist // すでに新規追加済みの場合は，重複して追加できない

        _newNotificationList.update { list ->
            // 一度追加して削除した後，再び追加した場合は「削除済みリスト」から取り除く
            deletedNotificationList.remove(periodAndTime)
            list.addedList(periodAndTime)
        }
        return NotificationState.Success
    }

    /**
     * UI操作による新規追加した通知の削除
     */
    fun removeNewNotification(periodAndTime: PeriodAndTime) {
        // 新規追加した通知の削除
        _newNotificationList.update {
            it.removedList(periodAndTime)
        }
        deletedNotificationList.add(periodAndTime)
    }

    /**
     * UI操作による既存通知の削除
     */
    fun removeExistingNotification(notificationEntity: NotificationEntity) {
        _existingNotificationList.update { it.removedList(notificationEntity) }
        deletedExistingNotificationList.add(notificationEntity)
    }

    fun save() = viewModelScope.launch {
        // TODO: 開始日よりも前に通知を設定できないようにする 
        val repellentEntity = getRepellent() // 新規登録/更新後のRepellentScheduleEntity
        val repellentId =
            if (isUpdate) updateRepellent(repellentEntity) else addRepellent(repellentEntity)

        // 既存の通知の削除
        deletedExistingNotificationList.forEach {
            deleteExistingNotificationEntity(it)
        }

        // 既存の通知の更新
        existingNotificationList.value.forEach {
            updateExistingNotificationEntity(
                original = it,
                updatedParent = repellentEntity
            )
        }

        // 新規追加した通知の追加処理
        newNotificationList.value.forEach {
            addNewNotificationEntity(
                finishDate = repellentEntity.finishDate,
                periodAndTime = it,
                parentId = repellentId,
                zoneId = repellentEntity.zoneId
            )
        }
    }

    fun delete() = viewModelScope.launch {
        if (repellent != null) repellentAction.delete(repellent)
        existingNotifications.forEach { deleteExistingNotificationEntity(it) }
    }

    /**
     * 虫除け記録をDBに追加する
     * @return 追加したRepellentScheduleEntityのid(プライマリキー)
     */
    private suspend fun addRepellent(repellentEntity: RepellentScheduleEntity): Long {
        val deferred = viewModelScope.async {
            repellentAction.insert(repellentEntity)
        }.await()
        return deferred
    }

    /**
     * DB内の虫除け記録を更新する
     * @return 更新したRepellentScheduleEntityのid(プライマリキー)
     */
    private suspend fun updateRepellent(repellentEntity: RepellentScheduleEntity): Long {
        viewModelScope.launch {
            repellentAction.update(repellentEntity)
        }
        return repellentEntity.id
    }

    /**
     * 新規追加した通知の登録
     * @param finishDate 虫除けが終わる日付
     * @param periodAndTime 虫除けが終わる何日前の何時に通知を送るかを指定するための情報
     */
    private suspend fun addNewNotificationEntity(
        finishDate: LocalDate,
        periodAndTime: PeriodAndTime,
        parentId: Long,
        zoneId: ZoneId
    ) {
        val notificationEntity = AppNotificationManager.createNotificationEntity(
            date = finishDate,
            time = periodAndTime.time,
            before = periodAndTime.period,
            parentId = parentId,
            zoneId = zoneId
        )
        notificationAction.insert(notificationEntity)
    }

    /**
     * 虫除け記録の(終了日やタイムゾーンの)更新による既存通知の更新
     * @param original 更新対象となる通知
     * @param updatedParent 更新後の虫除け記録
     */
    private suspend fun updateExistingNotificationEntity(
        original: NotificationEntity,
        updatedParent: RepellentScheduleEntity
    ) {
        val updatedNotificationEntity = AppNotificationManager
            .updateNotificationEntity(
                original = original,
                updatedDate = updatedParent.finishDate,
                updatedTime = original.schedule.time,
                updatedBefore = original.schedule.period,
                updatedZoneId = updatedParent.zoneId
            )
        notificationAction.update(updatedNotificationEntity)
    }

    /**
     * 既存通知の削除処理
     */
    private suspend fun deleteExistingNotificationEntity(target: NotificationEntity) =
        notificationAction.delete(target)

    /**
     * 新規追加する/更新した虫除け記録の情報(RepellentScheduleEntity)を取得する
     * この戻り値がDBに登録する記録
     */
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
 * 渡された期間を引いたLocalDateを返す
 */
private fun LocalDate.minusPeriod(simplePeriod: SimplePeriod): LocalDate =
    when (simplePeriod.periodUnit) {
        PeriodUnit.Day -> this.minusDays(simplePeriod.number.toLong())
        PeriodUnit.Week -> this.minusWeeks(simplePeriod.number.toLong())
        PeriodUnit.Month -> this.minusMonths(simplePeriod.number.toLong())
        PeriodUnit.Year -> this.minusYears(simplePeriod.number.toLong())
    }


/**
 * 重複を許さない要素の追加
 */
private fun <T> List<T>.addedList(item: T): List<T> {
    return if (item in this) this else (this + listOf(item))
}

/**
 * 渡されたitemを削除したリストを返す
 */
private fun <T> List<T>.removedList(item: T): List<T> {
    val original = this
    val tmp = mutableListOf<T>().apply { addAll(original) }
    return tmp.apply { remove(item) }
}

/**
 * NotificationEntity.scheduleと渡されたperiodAndTimeが一致するか
 */
private fun NotificationEntity.match(periodAndTime: PeriodAndTime): Boolean =
    this.schedule == periodAndTime

///**
// * 渡されたPeriodAndTimeと同じSimplePeriod, SimpleTimeを持つNotifyEntityのインデックス値を返す
// * 存在しない場合は-1が返される
// */
//private fun List<NotificationEntity>.indexOf(periodAndTime: PeriodAndTime): Int =
//    this.indexOfFirst { it.match(periodAndTime) }