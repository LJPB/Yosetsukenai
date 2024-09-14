package me.ljpb.yosetsukenai.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R

@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    itemName: String,
    item: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.row_item_horizontal_padding))
    ) {
        // 親RowにAlignment.CenterVerticallyを適用すると，渡されたitemのheightがdetail_row_heightを超えた場合に
        // IconとTextがDetailRowの中央に来てしまうが，これらはDetailRowの上部に置きたい
        // 一方，親RowにAlignment.Topを適用すると，IconがTextに対して中央に位置せずバランスが悪い(-_ ←こんな感じ。-- ←こうしたい)
        // そこで，IconとTextをRowでひとまとめにすることで，渡されたitemのheightに関わらず，IconとTextがDetailRowの上部に並び
        // IconはTextに対して中央に位置する。さらに，渡されたitemの開始Y座標が子Rowに揃うことでバランスが良くなる
        Row(
            modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = itemName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
        // itemが残りの全ての幅を埋め尽くして2つ目のSpacerのwidthが0となる場合に最低でも確保するpadding
        Spacer(modifier = Modifier.width(8.dp))
        Spacer(modifier = Modifier.weight(1f))
        item()
    }
}

@Composable
fun RowItemWithText(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    itemName: String,
    text: String,
    textOnClick: (() -> Unit)? = null
) {
    RowItemWithOneItem(
        modifier = modifier,
        leadingIcon = leadingIcon,
        itemName = itemName,
        item = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        itemOnClick = textOnClick
    )
}

@Composable
fun RowItemWithOneItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    itemName: String,
    item: @Composable () -> Unit,
    itemOnClick: (() -> Unit)? = null
) {
    val boxModifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height))
    RowItem(
        modifier = modifier,
        leadingIcon = leadingIcon,
        itemName = itemName,
        item = {
            Box(
                modifier = if (itemOnClick != null) {
                    boxModifier.clickable { itemOnClick() }
                } else {
                    boxModifier
                },
                contentAlignment = Alignment.Center
            ) {
                item()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RowItemWithTextPreview() {
    RowItemWithText(
        leadingIcon = Icons.Default.CalendarMonth,
        itemName = "itemName",
        text = "text"
    )
}

@Preview(showBackground = true)
@Composable
private fun RowItemWithOneItemPreview() {
    RowItemWithOneItem(
        leadingIcon = Icons.Default.CalendarMonth,
        itemName = "itemName",
        item = {
            PlaceTag(text = "item")
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RowItemPreview() {
    RowItem(
        leadingIcon = Icons.Default.CalendarMonth,
        itemName = "itemName",
        item = {
            Column {
                PlaceTag(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.row_item_height))
                        .width(64.dp)
                        .padding(vertical = 8.dp),
                    text = "item"
                )
                PlaceTag(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.row_item_height))
                        .width(64.dp)
                        .padding(vertical = 8.dp),
                    text = "item"
                )
                PlaceTag(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.row_item_height))
                        .width(64.dp)
                        .padding(vertical = 8.dp),
                    text = "item"
                )
            }
        }
    )
}