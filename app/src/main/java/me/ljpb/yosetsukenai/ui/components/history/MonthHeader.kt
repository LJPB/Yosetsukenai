package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import me.ljpb.yosetsukenai.R
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    calendarMonth: CalendarMonth,
    firstDayOfWeek: DayOfWeek,
    dayOfWeekTextStyle: androidx.compose.ui.text.TextStyle
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MonthTitle(
            yearMonth = calendarMonth.yearMonth,
            textStyle = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        DayOfWeekTitle(
            daysOfWeek = daysOfWeek(firstDayOfWeek),
            textStyle = dayOfWeekTextStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// 月の表示
// もし1月なら年も表示する
@Composable
private fun MonthTitle(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    textStyle: androidx.compose.ui.text.TextStyle,
    color: Color
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(R.string.year_and_month_title, yearMonth.year, yearMonth.monthValue),
        textAlign = TextAlign.Center,
        style = textStyle,
        color = color,
    )
}

@Preview(showBackground = true)
@Composable
private fun MonthTitlePreview() {
    MonthTitle(
        modifier = Modifier,
        YearMonth.now(),
        MaterialTheme.typography.bodyMedium,
        MaterialTheme.colorScheme.onSurface
    )
}

// 曜日の表示
@Composable
private fun DayOfWeekTitle(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek>,
    textStyle: androidx.compose.ui.text.TextStyle,
    color: Color
) {
    Row(modifier = modifier.fillMaxSize()) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier.weight(1f),
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = textStyle,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekTitlePreview() {
    DayOfWeekTitle(
        modifier = Modifier,
        daysOfWeek = daysOfWeek(),
        textStyle = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Preview(showBackground = true)
@Composable
private fun MonthHeaderPreview() {
    MonthHeader(
        modifier = Modifier,
        calendarMonth = CalendarMonth(
            YearMonth.now(),
            listOf()
        ),
        firstDayOfWeek = daysOfWeek().first(),
        dayOfWeekTextStyle = MaterialTheme.typography.titleSmall,
    )
}
