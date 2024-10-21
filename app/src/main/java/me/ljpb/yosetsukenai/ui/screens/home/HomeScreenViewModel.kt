package me.ljpb.yosetsukenai.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.ljpb.yosetsukenai.data.NotificationAction
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.notification.AppNotificationManager
import me.ljpb.yosetsukenai.ui.localDateAddPeriod
import java.time.LocalDate
import java.time.ZoneId

class HomeScreenViewModel(
    private val repellentAction: RepellentScheduleAction,
    private val notificationAction: NotificationAction,
    val currentDate: LocalDate
) : ViewModel() {
    private val _currentDateState = MutableStateFlow(currentDate)
    val currentDateState: StateFlow<LocalDate> = _currentDateState.asStateFlow()

    /**
     * 現在有効な虫除けのリスト
     */
    val validRepellentList: StateFlow<List<RepellentScheduleEntity>?> = repellentAction
        .getEnabledRepellents(currentDate)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    /**
     * 有効期限が過ぎて対応が必要な虫除けのリスト
     */
    val expiredRepellentList: StateFlow<List<RepellentScheduleEntity>?> = repellentAction
        .getExpiredRepellents(currentDate)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun reset(repellent: RepellentScheduleEntity) = viewModelScope.launch {
        /*
        // 既存の通知はスキップ
        skip(repellent)
         */

        // 日付を更新して同じデータの虫除けを追加する
        val startDate = LocalDate.now()
        val newRepellent = RepellentScheduleEntity(
            name = repellent.name,
            validityPeriod = repellent.validityPeriod,
            startDate = startDate,
            finishDate = localDateAddPeriod(startDate, repellent.validityPeriod),
            places = repellent.places,
            ignore = false,
            zoneId = ZoneId.systemDefault()
        )
        val newRepellentId = repellentAction.insert(newRepellent)

        // 何日前の何時に通知するかという情報を受け継いで、新規追加した虫除けに通知をセットする
        val notifications = notificationAction.getNotificationsByParentId(repellent.id).first()
        notifications.forEach {
            val notificationEntity = AppNotificationManager.createNotificationEntity(
                date = newRepellent.finishDate,
                time = it.schedule.time,
                before = it.schedule.period,
                parentId = newRepellentId,
                zoneId = newRepellent.zoneId
            )
            notificationAction.insert(notificationEntity)
        }
    }

    fun skip(repellent: RepellentScheduleEntity) = viewModelScope.launch {
        repellentAction.update(repellent.copy(ignore = true))
    }
}
