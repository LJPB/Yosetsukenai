package me.ljpb.yosetsukenai.ui.components.other

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.ui.components.common.RowItem

@Composable
fun OtherContentItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    RowItem(
        modifier = modifier.clickable(onClick = onClick),
        leadingIcon = icon,
        itemName = text,
        item = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun OtherContentItemPreview() {
    OtherContentItem(
        modifier = Modifier,
        icon = Icons.Default.Android,
        text = "Android"
    ) {}
}
