package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun Day(
    day: CalendarDay, 
    textStyle: TextStyle,
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = textStyle,
            color = when (day.position) {
                DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.outline
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayPreview() {
    Day(
        day = CalendarDay(LocalDate.now(), DayPosition.InDate),
        textStyle = MaterialTheme.typography.bodyLarge
    ) {}
}
