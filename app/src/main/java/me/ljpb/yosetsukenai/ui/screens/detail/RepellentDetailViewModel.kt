package me.ljpb.yosetsukenai.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.ljpb.yosetsukenai.data.InsectAction
import me.ljpb.yosetsukenai.data.NotificationAction
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

class RepellentDetailViewModel(
    val repellent: RepellentScheduleEntity,
    notificationAction: NotificationAction,
    insectAction: InsectAction
) : ViewModel() {
    val notifications: StateFlow<List<NotificationEntity>> = notificationAction
        .getNotificationsByParentId(repellent.id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

    val insects: StateFlow<List<InsectEntity>> = insectAction
        .getInsects(
            from = repellent.startDate,
            to = repellent.finishDate
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )
}