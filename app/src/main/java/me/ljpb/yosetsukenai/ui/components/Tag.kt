package me.ljpb.yosetsukenai.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun Tag(
    modifier: Modifier = Modifier,
    color: Color,
    text: String
) {
    Surface(
        modifier = modifier,
        color = color,
        shape = MaterialTheme.shapes.small,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    ),
                text = text,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
fun PlaceTag(
    modifier: Modifier = Modifier,
    text: String
) {
    Tag(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        text = text
    )
}

@Composable
fun InsectTag(
    modifier: Modifier = Modifier,
    text: String
) {
    Tag(
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        text = text
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