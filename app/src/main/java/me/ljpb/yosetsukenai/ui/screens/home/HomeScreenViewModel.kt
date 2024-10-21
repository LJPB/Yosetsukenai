package me.ljpb.yosetsukenai.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate

class HomeScreenViewModel(
    private val repellentAction: RepellentScheduleAction,
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

    fun reset(repellent: RepellentScheduleEntity) {

    }

    fun skip(repellent: RepellentScheduleEntity) = viewModelScope.launch {
        repellentAction.update(repellent.copy(ignore = true))
    }
}
