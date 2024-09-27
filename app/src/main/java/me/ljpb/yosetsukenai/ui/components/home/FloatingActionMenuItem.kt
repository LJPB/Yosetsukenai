package me.ljpb.yosetsukenai.ui.components.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.ConstIcon

/**
 * FABをラベルとともに表示するコンポーザブル
 * @param icon FABに表示するアイコン
 * @param label ラベルテキスト。nullでラベルを表示しない。
 * @param contentDescription iconの説明テキスト
 * @param onClick FABを押した時のイベント
 */
@Composable
fun FloatingActionMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String?,
    containerColor: Color,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label != null) {
            FabLabel(label = label)
            Spacer(modifier = Modifier.width(16.dp))
        }
        Fab(
            icon = icon,
            containerColor = containerColor,
            contentDescription = contentDescription,
            onClick = onClick
        )
    }
}

@Composable
private fun Fab(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    containerColor: Color,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = containerColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun FabLabel(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.inverseSurface,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = label,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun FabPreview() {
    Fab(
        icon = ConstIcon.FAB_ADD_INSECT,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentDescription = stringResource(R.string.fab_label_add_insect)
    ) {

    }
}

@Preview(showBackground = true)
@Composable
private fun FabLabelPreview() {
    FabLabel(label = "label")
}

@Preview(showBackground = true)
@Composable
private fun FloatingActionMenuItemPreview() {
    FloatingActionMenuItem(
        icon = ConstIcon.FAB_ADD_REPELLENT,
        label = stringResource(R.string.fab_label_add_repellent),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentDescription = stringResource(R.string.fab_label_add_repellent)
    ) {}
}