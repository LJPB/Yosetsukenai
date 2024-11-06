package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun Day(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    textStyle: TextStyle,
    onClick: ((CalendarDay) -> Unit)? = null
) {
    val containerColor: Color
    val textColor: Color
    val isMonthDate = day.position == DayPosition.MonthDate

    if (!isMonthDate) { // 月に含まれない場合 
        // たとえば1日の前に先月の31日を表示したり、31日の次に翌月の1日を表示したりする場合は薄く表示する
        containerColor = Color.Transparent
        textColor = MaterialTheme.colorScheme.outline
    } else {
        if (onClick != null) {
            containerColor = MaterialTheme.colorScheme.primaryContainer
            textColor = MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            containerColor = Color.Transparent
            textColor = MaterialTheme.colorScheme.onSurface
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
        ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = { if (isMonthDate && onClick != null) onClick(day) },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = containerColor)
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = textStyle,
                color = textColor,
            )
        }
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
