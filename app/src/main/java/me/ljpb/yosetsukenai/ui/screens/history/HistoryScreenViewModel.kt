package me.ljpb.yosetsukenai.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ljpb.yosetsukenai.data.InsectAction
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class HistoryScreenViewModel(
    private val repellentAction: RepellentScheduleAction,
    private val insectAction: InsectAction
) : ViewModel() {
    private val insectMinDate = insectAction.getMinDate()
    private val insectMaxDate = insectAction.getMaxDate()
    private val repellentMinDate = repellentAction.getMinDate()
    private val repellentMaxDate = repellentAction.getMaxDate()

    private val _startMonthLoading = MutableStateFlow(true)
    val startMonthLoading = _startMonthLoading.asStateFlow()
    private val _endMonthLoading = MutableStateFlow(true)
    val endMonthLoading = _endMonthLoading.asStateFlow()

    val currentMonth: YearMonth = YearMonth.now()
    var bottomSheetDate: LocalDate = LocalDate.now()

    // 虫除けの開始日、虫の発見日のうち最小のもの
    val startMonth =
        combine(insectMinDate, repellentMinDate) { _insectMinDate, _repellentMinDate ->
            _startMonthLoading.update { true }
            val insectMin = _insectMinDate ?: LocalDate.now()
            val repellentMin = _repellentMinDate ?: LocalDate.now()
            val minDate = getMinLocalDate(insectMin, repellentMin)
            _startMonthLoading.update { false }
            currentMonth.minusMonths(
                ChronoUnit.MONTHS.between(minDate, LocalDate.now())
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            currentMonth
        )

    // 虫除けの開始日、虫の発見日のうち最大のもの
    val endMonth =
        combine(insectMaxDate, repellentMaxDate) { _insectMaxDate, _repellentMaxDate ->
            _endMonthLoading.update { true }
            val insectMax = _insectMaxDate ?: LocalDate.now()
            val repellentMax = _repellentMaxDate ?: LocalDate.now()
            val maxDate = getMaxLocalDate(insectMax, repellentMax)
            _endMonthLoading.update { false }
            currentMonth.minusMonths(
                ChronoUnit.MONTHS.between(maxDate, LocalDate.now())
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            currentMonth
        )

    // LocalDateの日付に登録された記録があればtrueなければfalseを格納するマップ
    private val _days: MutableStateFlow<HashMap<LocalDate, StateFlow<Boolean>>> =
        MutableStateFlow(hashMapOf())

    val days: StateFlow<HashMap<LocalDate, StateFlow<Boolean>>> = _days.asStateFlow()

    private val _repellentList = MutableStateFlow<List<RepellentScheduleEntity>>(listOf())
    val repellentList = _repellentList.asStateFlow()

    private val _insectList = MutableStateFlow<List<InsectEntity>>(listOf())
    val insectList = _insectList.asStateFlow()

    /**
     * 渡されたdateの日付に虫除け/発見した虫が記録されているかを表すフラグをマップ(days)に格納するためのメソッド
     */
    fun load(date: LocalDate) {
        val existState = _days.value[date]
        if (existState == null) {
            val repellentCount = repellentAction.countByDate(date)
            val insectCount = insectAction.countByDate(date)
            val state = combine(repellentCount, insectCount) { _repellentCount, _insectCount ->
                // @@@Countは渡されたdateの日付に記録した@@@の個数
                // これらの少なくとも一方が0でなければ、つまりこれらの和が0でなければ渡されたdateの日付に何らかの記録をしたことになる
                _repellentCount + _insectCount != 0
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                false
            )
            _days.update {
                it.apply {
                    it[date] = state
                }
            }
        }
    }

    /**
     * 渡されたdateの日付に登録した虫除け/虫の記録を読み込む
     */
    fun loadList(date: LocalDate) = viewModelScope.launch {
        bottomSheetDate = date
        _repellentList.update {
            repellentAction.getItems(date).first()
        }
        _insectList.update {
            insectAction.getInsects(date, date).first()
        }
    }
}

private fun getMinLocalDate(date1: LocalDate, date2: LocalDate): LocalDate =
    if (date1.isBefore(date2)) date1 else date2

private fun getMaxLocalDate(date1: LocalDate, date2: LocalDate): LocalDate =
    if (date1.isAfter(date2)) date1 else date2
