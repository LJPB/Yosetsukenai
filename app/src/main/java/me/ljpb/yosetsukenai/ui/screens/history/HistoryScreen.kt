package me.ljpb.yosetsukenai.ui.screens.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.ViewModelProvider
import me.ljpb.yosetsukenai.ui.components.history.BottomSheetContent
import me.ljpb.yosetsukenai.ui.components.history.Calendar
import me.ljpb.yosetsukenai.ui.components.home.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryScreenViewModel = viewModel(factory = ViewModelProvider.historyViewModel()),
    repellentOnClick: (RepellentScheduleEntity) -> Unit,
    insectOnClick: (InsectEntity) -> Unit,
) {
    val currentMonth = viewModel.currentMonth
    val startMonth by viewModel.startMonth.collectAsState()
    val startMonthLoading by viewModel.startMonthLoading.collectAsState()
    val endMonth by viewModel.endMonth.collectAsState()
    val endMonthLoading by viewModel.endMonthLoading.collectAsState()

    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val repellentList by viewModel.repellentList.collectAsState()
    val insectList by viewModel.insectList.collectAsState()

    if (startMonthLoading || endMonthLoading) {
        LoadingContent()
    } else {
        Calendar(
            modifier = modifier,
            calendarState = calendarState,
            viewModel = viewModel
        ) {
            viewModel.loadList(it.date)
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                BottomSheetContent(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    date = viewModel.bottomSheetDate,
                    repellentList = repellentList,
                    insectList = insectList,
                    repellentOnClick = {
                        showBottomSheet = false
                        repellentOnClick(it)
                    },
                    insectOnClick = {
                        showBottomSheet = false
                        insectOnClick(it)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(
        modifier = Modifier,
        viewModel = viewModel(),
        repellentOnClick = {},
        insectOnClick = {}
    )
}
