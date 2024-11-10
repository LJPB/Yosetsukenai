package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.ljpb.yosetsukenai.MyApplication
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.screens.detail.RepellentDetailViewModel
import me.ljpb.yosetsukenai.ui.screens.edit.InsectEditViewModel
import me.ljpb.yosetsukenai.ui.screens.edit.RepellentEditViewModel
import me.ljpb.yosetsukenai.ui.screens.history.HistoryScreenViewModel
import me.ljpb.yosetsukenai.ui.screens.home.HomeScreenViewModel
import java.time.LocalDate

object ViewModelProvider {
    fun homeScreenViewModel(currentDate: LocalDate) =
        viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApplication
                val container = app.dbRepositoryContainer
                HomeScreenViewModel(
                    repellentAction = container.repellentScheduleRepository,
                    notificationAction = container.notificationRepository,
                    currentDate = currentDate
                )
            }
        }

    fun repellentEditViewModel(
        repellent: RepellentScheduleEntity?,
        notifications: List<NotificationEntity>
    ) =
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

    fun repellentDetailViewModel(repellent: RepellentScheduleEntity) =
        viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApplication
                val container = app.dbRepositoryContainer
                RepellentDetailViewModel(
                    repellent = repellent,
                    notificationAction = container.notificationRepository,
                    insectAction = container.insectRepository,
                )
            }
        }

    fun insectEditViewModel(insect: InsectEntity?) =
        viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApplication
                val container = app.dbRepositoryContainer
                InsectEditViewModel(
                    insect = insect,
                    insectAction = container.insectRepository
                )
            }
        }

    fun historyViewModel() = viewModelFactory {
        initializer {
            val app = this[APPLICATION_KEY] as MyApplication
            val container = app.dbRepositoryContainer
            HistoryScreenViewModel(
                repellentAction = container.repellentScheduleRepository,
                insectAction = container.insectRepository
            )
        }
    }
}
