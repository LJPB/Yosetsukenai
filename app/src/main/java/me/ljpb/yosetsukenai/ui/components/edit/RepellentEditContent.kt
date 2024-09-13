package me.ljpb.yosetsukenai.ui.components.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.RowItemWithOneItem
import me.ljpb.yosetsukenai.ui.components.SimpleTextField
import java.time.LocalDate

@Composable
fun RepellentEditContent(
    modifier: Modifier = Modifier,
    name: String,
    startDate: LocalDate,
    endDate: LocalDate,
    validityPeriodText: String,
    insects: List<String>,
    places: List<String>,
    notifies: List<String>
) {
    var str by remember { mutableStateOf("") }
    val textStyle = MaterialTheme.typography.titleMedium
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.detail_content_vertical_padding))
    ) {
        // 商品名
        HorizontalDivider()
        RowItemWithOneItem(
            modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
            leadingIcon = ConstIcon.PRODUCT_NAME, 
            itemName = stringResource(id = R.string.repellent_name), 
            item = {
                SimpleTextField(
                    value = str,
                    onValueChange = { str = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                    placeholderText = "placeholder",
                    placeholderTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        // 開始日
        HorizontalDivider()
        // 終了日
        HorizontalDivider()
        // 有効期間
        HorizontalDivider()
        // 発見した虫の一覧
        HorizontalDivider()
        // 場所
        HorizontalDivider()
        // 通知
    }
}

@Preview(showBackground = true)
@Composable
private fun RepellentEditPreview() {
    RepellentEditContent(
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