package me.ljpb.yosetsukenai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import java.time.LocalDate

@Composable
fun ValidRepellentCard(
    modifier: Modifier = Modifier,
    startDate: LocalDate,
    endDate: LocalDate,
    currentDate: LocalDate,
    name: String,
    validityPeriodText: String,
    places: List<String>,
    resetOnClick: () -> Unit,
    cardOnClick: () -> Unit,
) {
    // 虫除けが有効な日数
    val validityPeriodDays = endDate.toEpochDay() - startDate.toEpochDay()
    // 開始からの経過日数
    val elapsedDays = currentDate.toEpochDay() - startDate.toEpochDay()
    // 経過の割合
    val progressPercentage = elapsedDays / validityPeriodDays.toFloat()
    RepellentCardContent(
        modifier = modifier,
        name = name,
        validityPeriodText = validityPeriodText,
        places = places,
        progress = {
            ValidRepellentProgress(
                progress = progressPercentage,
                startText = stringResource(
                    id = R.string.formated_date,
                    startDate.year,
                    startDate.monthValue,
                    startDate.dayOfMonth,
                ),
                middleText = stringResource(
                    id = R.string.until,
                    endDate.toEpochDay() - currentDate.toEpochDay()
                ),
                endText = stringResource(
                    id = R.string.formated_date,
                    endDate.year,
                    endDate.monthValue,
                    endDate.dayOfMonth,
                )
            )
        },
        footerEndOnClick = resetOnClick
    ) {
        cardOnClick()
    }
}

@Composable
fun ExpiredRepellentCard(
    modifier: Modifier = Modifier,
    startDate: LocalDate,
    endDate: LocalDate,
    name: String,
    validityPeriodText: String,
    places: List<String>,
    skipOnClick: () -> Unit,
    resetOnClick: () -> Unit,
    cardOnClick: () -> Unit,
) {
    RepellentCardContent(
        modifier = modifier,
        name = name,
        icon = Icons.Default.Warning,
        validityPeriodText = validityPeriodText,
        places = places,
        progress = {
            ExpiredRepellentProgress(
                startText = stringResource(
                    id = R.string.formated_date,
                    startDate.year,
                    startDate.monthValue,
                    startDate.dayOfMonth,
                ),
                middleText = stringResource(R.string.expired),
                endText = stringResource(
                    id = R.string.formated_date,
                    endDate.year,
                    endDate.monthValue,
                    endDate.dayOfMonth,
                )
            )
        },
        footerStartOnClick = skipOnClick,
        footerEndOnClick = resetOnClick
    ) {
        cardOnClick()
    }
}

/**
 * @param validityPeriodText 有効期間を表すテキスト (例：30日間，1年8ヶ月)
 */
@Composable
private fun RepellentCardContent(
    modifier: Modifier = Modifier,
    name: String,
    icon: ImageVector? = null,
    validityPeriodText: String,
    places: List<String>,
    progress: @Composable () -> Unit,
    footerStartOnClick: (() -> Unit)? = null,
    footerEndOnClick: () -> Unit,
    onClick: () -> Unit,
) {
    val cardHorizontalPadding =
        dimensionResource(id = R.dimen.repellent_card_content_horizontal_padding)
    val cardVerticalPadding =
        dimensionResource(id = R.dimen.repellent_card_content_vertical_padding)
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = cardHorizontalPadding)
        ) {
            // 商品名を表示
            // 期限切れの場合はエラーアイコンを表示
            Row(
                modifier = Modifier.padding(top = cardVerticalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (icon != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 有効期間を表示
            Text(
                text = stringResource(id = R.string.validity_period_text, validityPeriodText),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 場所を表示するタグ
            LazyRow(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                items(places) { place ->
                    PlaceTag(
                        modifier = Modifier.padding(end = 8.dp),
                        text = place
                    )
                }
            }
            progress()
            // footer
            Row(
                modifier = Modifier.padding(top = 16.dp, bottom = cardVerticalPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (footerStartOnClick != null) {
                    TextButton(onClick = footerStartOnClick) {
                        Text(
                            text = stringResource(id = R.string.skip),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = footerEndOnClick) {
                    Text(
                        text = stringResource(id = R.string.reset_repellent),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


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

@Preview
@Composable
private fun ValidRepellentCardPreview() {
    ValidRepellentCard(
        startDate = LocalDate.of(2024, 9, 1),
        endDate = LocalDate.of(2024, 10, 1),
        currentDate = LocalDate.of(2024, 9, 10),
        name = "商品名",
        validityPeriodText = "30日間",
        places = listOf("場所1", "場所2", "場所3"),
        resetOnClick = { /*TODO*/ }) {
    }
}

@Preview
@Composable
private fun ExpiredRepellentCardPreview() {
    ExpiredRepellentCard(
        startDate = LocalDate.of(2024, 9, 1),
        endDate = LocalDate.of(2024, 10, 1),
        name = "商品名",
        validityPeriodText = "30日間",
        places = listOf("場所1", "場所2", "場所3"),
        skipOnClick = {},
        resetOnClick = { /*TODO*/ }) {
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