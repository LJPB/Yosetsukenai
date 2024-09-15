package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.ljpb.yosetsukenai.MyApplication
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

object ViewModelProvider {
    fun repellentEditViewModel(repellent: RepellentScheduleEntity?, notifies: List<NotifyEntity>) =
        viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApplication
                val container = app.dbRepositoryContainer
                RepellentEditViewModel(
                    repellent = repellent,
                    notifications = notifies,
                    repellentAction = container.repellentScheduleRepository,
                    notifyAction = container.notifyRepository
                )
            }
        }
}