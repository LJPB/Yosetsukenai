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
import java.time.LocalDate

/**
 * 有効な虫除けのリスト
 */
@Composable
fun ValidRepellentTabContent(
    modifier: Modifier = Modifier,
    validRepellentList: List<RepellentScheduleEntity>?,
    currentDate: LocalDate,
    resetOnClick: (RepellentScheduleEntity) -> Unit,
    cardOnClick: (RepellentScheduleEntity) -> Unit
) {
    if (validRepellentList == null) {
        LoadingContent(
            modifier = modifier,
            bottomPadding = dimensionResource(R.dimen.tab_item_height)
        )
    } else {
        ValidRepellentTabBody(
            modifier = modifier,
            repellentList = validRepellentList,
            currentDate = currentDate,
            resetOnClick = resetOnClick,
            cardOnClick = cardOnClick
        )
    }
}

@Composable
private fun ValidRepellentTabBody(
    modifier: Modifier = Modifier,
    repellentList: List<RepellentScheduleEntity>,
    currentDate: LocalDate,
    resetOnClick: (RepellentScheduleEntity) -> Unit,
    cardOnClick: (RepellentScheduleEntity) -> Unit,
) {
    if (repellentList.isNotEmpty()) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.home_screen_card_vertical_padding))
        ) {
            repellentList.forEach {
                ValidRepellentCard(
                    repellent = it,
                    currentDate = currentDate,
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
private fun ValidRepellentTabBodyPreview() {
    ValidRepellentTabBody(
        repellentList = listOf(),
        currentDate = LocalDate.now(),
        resetOnClick = {},
        cardOnClick = {}
    )
}