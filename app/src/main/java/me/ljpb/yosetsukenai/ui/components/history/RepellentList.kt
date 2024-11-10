package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun RepellentList(
    modifier: Modifier = Modifier,
    listTitleTextStyle: TextStyle,
    textStyle: TextStyle,
    list: List<RepellentScheduleEntity>,
    onClick: (RepellentScheduleEntity) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = stringResource(R.string.bottom_sheet_repellent_list_title),
            style = listTitleTextStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        list.forEach {
            ListItem(
                modifier = Modifier.padding(vertical = 4.dp),
                textStyle = textStyle,
                itemName = it.name,
                onClick = { onClick(it) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepellentListPreview() {
    RepellentList(
        modifier = Modifier,
        listTitleTextStyle = MaterialTheme.typography.titleLarge,
        textStyle = MaterialTheme.typography.bodyLarge,
        list = listOf(
            RepellentScheduleEntity(
                name = "name",
                validityPeriod = SimplePeriod.ofDays(1),
                startDate = LocalDate.now(),
                finishDate = LocalDate.now(),
                places = listOf("place1", "place2", "place3"),
                ignore = false,
                zoneId = ZoneId.systemDefault()
            ),
            RepellentScheduleEntity(
                name = "name",
                validityPeriod = SimplePeriod.ofDays(1),
                startDate = LocalDate.now(),
                finishDate = LocalDate.now(),
                places = listOf("place1", "place2", "place3"),
                ignore = false,
                zoneId = ZoneId.systemDefault()
            ),
            RepellentScheduleEntity(
                name = "name",
                validityPeriod = SimplePeriod.ofDays(1),
                startDate = LocalDate.now(),
                finishDate = LocalDate.now(),
                places = listOf("place1", "place2", "place3"),
                ignore = false,
                zoneId = ZoneId.systemDefault()
            ),
            RepellentScheduleEntity(
                name = "name",
                validityPeriod = SimplePeriod.ofDays(1),
                startDate = LocalDate.now(),
                finishDate = LocalDate.now(),
                places = listOf("place1", "place2", "place3"),
                ignore = false,
                zoneId = ZoneId.systemDefault()
            )
        )
    ) {}
}
