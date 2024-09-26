package me.ljpb.yosetsukenai.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

/**
 * 期限が切れて対応が必要な虫除けのリスト
 */
@Composable
fun ExpiredRepellentTabContent(
    modifier: Modifier = Modifier,
    expiredRepellentList: List<RepellentScheduleEntity>?,
    skipOnClick: (RepellentScheduleEntity) -> Unit,
    resetOnClick: (RepellentScheduleEntity) -> Unit,
    cardOnClick: (RepellentScheduleEntity) -> Unit
) {
    if (expiredRepellentList == null) {
        LoadingContent(
            modifier = modifier,
            bottomPadding = dimensionResource(R.dimen.tab_item_height)
        )
    } else {
        ExpiredRepellentTabBody(
            modifier = modifier,
            repellentList = expiredRepellentList,
            resetOnClick = resetOnClick,
            cardOnClick = cardOnClick,
            skipOnClick = skipOnClick
        )
    }
}

@Composable
private fun ExpiredRepellentTabBody(
    modifier: Modifier = Modifier,
    repellentList: List<RepellentScheduleEntity>,
    skipOnClick: (RepellentScheduleEntity) -> Unit,
    resetOnClick: (RepellentScheduleEntity) -> Unit,
    cardOnClick: (RepellentScheduleEntity) -> Unit,
) {
    if (repellentList.isNotEmpty()) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.home_screen_card_vertical_padding))
        ) {
            repellentList.forEach {
                ExpiredRepellentCard(
                    repellent = it,
                    skipOnClick = { skipOnClick(it) },
                    resetOnClick = { resetOnClick(it) },
                    cardOnClick = { cardOnClick(it) },
                )
            }
        }
    } else {
        RepellentEmpty(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(R.string.expired_not_found),
            bottomPadding = dimensionResource(R.dimen.tab_item_height)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpiredRepellentTabBodyPreview() {
    ExpiredRepellentTabBody(
        repellentList = listOf(),
        skipOnClick = {},
        resetOnClick = {},
        cardOnClick = {}
    )
}