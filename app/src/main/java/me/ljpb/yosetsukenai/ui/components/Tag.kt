package me.ljpb.yosetsukenai.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun Tag(
    modifier: Modifier = Modifier,
    containerColor: Color,
    textColor: Color,
    text: String,
    enabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    InputChip(
        modifier = modifier,
        selected = true, 
        enabled = enabled,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor,
            selectedContainerColor = containerColor,
            disabledSelectedContainerColor = containerColor,
        )
    )
}

@Composable
fun PlaceTag(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    Tag(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        text = text,
        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun InsectTag(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    Tag(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        text = text,
        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
        enabled = enabled,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceTagPreview() {
    PlaceTag(
        modifier = Modifier.padding(8.dp),
        text = "aaa"
    )
}

@Preview(showBackground = true)
@Composable
private fun InsectTagPreview() {
    InsectTag(
        modifier = Modifier.padding(8.dp),
        text = "aaa"
    )
}