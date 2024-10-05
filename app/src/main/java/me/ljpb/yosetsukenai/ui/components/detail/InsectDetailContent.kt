package me.ljpb.yosetsukenai.ui.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.common.RowItemWithText
import me.ljpb.yosetsukenai.ui.getTextOfLocalDate
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun InsectDetailContent(
    modifier: Modifier = Modifier,
    insect: InsectEntity
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.detail_content_vertical_padding))
    ) {
        // 発見した虫の名前
        RowItemWithText(
            leadingIcon = ConstIcon.INSECT_NAME,
            itemName = stringResource(id = R.string.insect_name),
            text = insect.name
        )
        HorizontalDivider()

        // 発見日
        RowItemWithText(
            leadingIcon = ConstIcon.INSECT_DATE,
            itemName = stringResource(id = R.string.insect_date),
            text = getTextOfLocalDate(insect.date, context)
        )
        HorizontalDivider()

        // 発見した虫の大きさ
        RowItemWithText(
            leadingIcon = ConstIcon.INSECT_SIZE,
            itemName = stringResource(id = R.string.insect_size),
            text = insect.size
        )
        HorizontalDivider()

        // 発見した虫の状態
        RowItemWithText(
            leadingIcon = ConstIcon.INSECT_CONDITION,
            itemName = stringResource(id = R.string.insect_condition),
            text = insect.condition
        )
        HorizontalDivider()

        // 虫を発見した場所
        RowItemWithText(
            leadingIcon = ConstIcon.INSECT_PLACE,
            itemName = stringResource(id = R.string.insect_place),
            text = insect.place
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InsectDetailContentPreview() {
    InsectDetailContent(
        modifier = Modifier,
        insect = InsectEntity(
            name = "insect",
            date = LocalDate.now(),
            size = "big",
            condition = "good",
            place = "entrance",
            zoneId = ZoneId.systemDefault()
        )
    )
}
