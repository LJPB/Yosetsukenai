package me.ljpb.yosetsukenai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ValidRepellentProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    startText: String,
    middleText: String,
    endText: String,
) {
    RepellentProgress(
        modifier = modifier,
        progress = progress,
        barColor = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.primaryContainer,
        startText = startText,
        middleText = middleText,
        endText = endText,
    )
}

@Composable
fun ExpiredRepellentProgress(
    modifier: Modifier = Modifier,
    startText: String,
    middleText: String,
    endText: String,
) {
    RepellentProgress(
        modifier = modifier,
        progress = 1f,
        barColor = MaterialTheme.colorScheme.error,
        trackColor = MaterialTheme.colorScheme.primaryContainer,
        startText = startText,
        middleText = middleText,
        endText = endText,
    )
}

@Composable
private fun RepellentProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    barColor: Color,
    trackColor: Color,
    startText: String,
    middleText: String,
    endText: String,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 開始日
            Text(
                text = startText,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            // 残り日数/期限切れ
            Text(
                text = middleText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            // 終了日
            Text(
                text = endText,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        // プログレスバー
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { progress },
            color = barColor,
            trackColor = trackColor,
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ValidRepellentProgressPreview() {
    ValidRepellentProgress(
        progress = 0.5f,
        startText = "2024年09月10日",
        middleText = "残り20日",
        endText = "2024年09月30日"
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpiredRepellentProgressPreview() {
    ExpiredRepellentProgress(
        startText = "2024年08月10日",
        middleText = "期限切れ",
        endText = "2024年09月09日"
    )
}