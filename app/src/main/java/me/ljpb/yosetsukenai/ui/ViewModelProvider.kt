package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.ljpb.yosetsukenai.MyApplication
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.screens.RepellentEditViewModel

object ViewModelProvider {
    fun repellentEditViewModel(repellent: RepellentScheduleEntity?, notifications: List<NotificationEntity>) =
        viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApplication
                val container = app.dbRepositoryContainer
                RepellentEditViewModel(
                    repellent = repellent,
                    existingNotifications = notifications,
                    repellentAction = container.repellentScheduleRepository,
                    notificationAction = container.notificationRepository
                )
            }
        }
}