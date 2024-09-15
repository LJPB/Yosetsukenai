package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.components.edit.RepellentEditViewModel

object ViewModelProvider {
    fun repellentEditViewModel(repellent: RepellentScheduleEntity?, notifies: List<NotifyEntity>) =
        viewModelFactory {
            initializer {
                RepellentEditViewModel(
                    repellent = repellent,
                    notifies = notifies
                )
            }
        }
}