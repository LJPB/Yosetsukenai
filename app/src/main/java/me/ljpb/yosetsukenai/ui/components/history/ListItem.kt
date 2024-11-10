package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    itemName: String,
    textStyle: TextStyle,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick()
            }
    ) {
        Text(
            style = textStyle,
            text = "・"
        )
        Text(
            modifier = Modifier.weight(1f),
            maxLines = 1,
            textAlign = TextAlign.Start,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary,
            text = itemName
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListContentPreview() {
    ListItem(
        modifier = Modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        itemName = "name"
    ) {}
}
