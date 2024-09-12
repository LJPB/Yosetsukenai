package me.ljpb.yosetsukenai.ui.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.components.ConstIcon
import me.ljpb.yosetsukenai.ui.components.InsectTag
import me.ljpb.yosetsukenai.ui.components.PlaceTag
import me.ljpb.yosetsukenai.ui.components.RowItem
import me.ljpb.yosetsukenai.ui.components.RowItemWithText
import java.time.LocalDate

@Composable
fun RepellentDetailContent(
    modifier: Modifier = Modifier,
    name: String,
    startDate: LocalDate,
    endDate: LocalDate,
    validityPeriodText: String,
    insects: List<String>,
    places: List<String>,
    notifies: List<String>
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.detail_content_vertical_padding))
    ) {
        // 商品名
        RowItemWithText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.PRODUCT_NAME,
            itemName = stringResource(id = R.string.repellent_name),
            text = name
        )
        HorizontalDivider()
        // 開始日
        RowItemWithText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.START_DATE,
            itemName = stringResource(id = R.string.repellent_start_date),
            text = stringResource(
                id = R.string.formated_date,
                startDate.year,
                startDate.monthValue,
                startDate.dayOfMonth
            )
        )
        HorizontalDivider()
        // 終了日
        RowItemWithText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.END_DATE,
            itemName = stringResource(id = R.string.repellent_end_date),
            text = stringResource(
                id = R.string.formated_date,
                endDate.year,
                endDate.monthValue,
                endDate.dayOfMonth
            )
        )
        HorizontalDivider()
        // 有効期間
        RowItemWithText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.VALIDITY_PERIOD,
            itemName = stringResource(id = R.string.repellent_validity_period),
            text = validityPeriodText
        )
        HorizontalDivider()
        // 発見した虫の一覧
        RowItem(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.INSECT,
            itemName = stringResource(id = R.string.repellent_insect)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                insects.forEach { insect ->
                    InsectTag(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.row_item_height))
                            .padding(vertical = 4.dp),
                        text = insect
                    )
                }
            }
        }
        HorizontalDivider()
        // 場所
        RowItem(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.PLACE,
            itemName = stringResource(id = R.string.repellent_place)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                places.forEach { place ->
                    PlaceTag(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.row_item_height))
                            .padding(vertical = 4.dp),
                        text = place
                    )
                }
            }
        }
        HorizontalDivider()
        // 通知
        RowItem(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.detail_content_horizontal_padding)),
            leadingIcon = ConstIcon.NOTIFY,
            itemName = stringResource(id = R.string.repellent_notify)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                notifies.forEach { notify ->
                    InsectTag(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.row_item_height))
                            .padding(vertical = 4.dp),
                        text = notify
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepellentDetailPreview() {
    RepellentDetailContent(
        name = "商品名",
        startDate = LocalDate.of(2024, 9, 1),
        endDate = LocalDate.of(2024, 10, 1),
        validityPeriodText = "30日間",
        insects = emptyList(),
        places = listOf(
            "玄関",
            "窓辺",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        ),
        notifies = listOf("当日(10:00)", "前日(12:00)")
    )
}