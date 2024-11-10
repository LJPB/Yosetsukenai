package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import me.ljpb.yosetsukenai.ui.screens.history.HistoryScreenViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    viewModel: HistoryScreenViewModel,
    onDayClick: (CalendarDay) -> Unit,
) {
    val dayTextStyle = MaterialTheme.typography.bodyMedium
    val days by viewModel.days.collectAsState()
    val dayStates = hashMapOf<LocalDate, State<Boolean>>()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        VerticalCalendar(
            modifier = Modifier.fillMaxSize(),
            state = calendarState,
            monthHeader = {
                MonthHeader(
                    calendarMonth = it,
                    firstDayOfWeek = calendarState.firstDayOfWeek,
                    dayOfWeekTextStyle = dayTextStyle
                )
            },
            dayContent = {
                viewModel.load(it.date)
                dayStates[it.date] = days[it.date]!!.collectAsState()
                val state by dayStates[it.date]!!
                Day(
                    modifier = Modifier.padding(4.dp),
                    day = it,
                    textStyle = dayTextStyle,
                    clickState = state,
                    onClick = onDayClick
                )
            },
            monthContainer = { _, container ->
                Column(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    container()
                    HorizontalDivider()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12 * 10) }
    val endMonth = remember { currentMonth.plusMonths(12 * 10) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
//    Calendar(
//        modifier = Modifier,
//        calendarState = state
//    )
}
