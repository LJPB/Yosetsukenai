package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    date: LocalDate,
    repellentList: List<RepellentScheduleEntity>,
    insectList: List<InsectEntity>,
    repellentOnClick: (RepellentScheduleEntity) -> Unit,
    insectOnClick: (InsectEntity) -> Unit,
) {
    val listTitleTextStyle = MaterialTheme.typography.titleMedium
    val textStyle = MaterialTheme.typography.bodyLarge
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = stringResource(
                R.string.bottom_sheet_title,
                stringResource(
                    R.string.formated_date,
                    date.year,
                    date.monthValue,
                    date.dayOfMonth
                )
            ),
            style = MaterialTheme.typography.titleLarge
        )

        if (repellentList.isNotEmpty()) {
            RepellentList(
                modifier = Modifier.padding(vertical = 8.dp),
                listTitleTextStyle = listTitleTextStyle,
                textStyle = textStyle,
                list = repellentList,
                onClick = repellentOnClick
            )
        }

        if (insectList.isNotEmpty()) {
            if (repellentList.isNotEmpty()) Spacer(modifier = Modifier.height(8.dp))
            InsectList(
                modifier = Modifier.padding(vertical = 8.dp),
                listTitleTextStyle = listTitleTextStyle,
                textStyle = textStyle,
                list = insectList,
                onClick = insectOnClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetContentPreview() {

}