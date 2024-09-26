package me.ljpb.yosetsukenai.ui.components.home

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.screens.home.HomeScreenViewModel
import java.time.LocalDate

private const val VALID_TAB = 0
private const val EXPIRED_TAB = 1
private const val HISTORY_TAB = 2
private const val OTHERS_TAB = 3

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    cardOnClick: (RepellentScheduleEntity) -> Unit,
) {
    val tabTitleList = listOf(
        stringResource(R.string.valid_tab_title_text),
        stringResource(R.string.expired_tab_title_text),
        stringResource(R.string.history_tab_title_text),
        stringResource(R.string.others_tab_title_text),
    )
    val validRepellentList by homeScreenViewModel.validRepellentList.collectAsState()
    val expiredRepellentList by homeScreenViewModel.expiredRepellentList.collectAsState()
    TabContainer(
        modifier = modifier,
        defaultIndex = if (expiredRepellentList?.isNotEmpty() == true) EXPIRED_TAB else VALID_TAB, // 期限切れのリストがあれば最初に表示する
        errorIndex = if (expiredRepellentList?.isNotEmpty() == true) EXPIRED_TAB else -1, // 期限切れのリストがあれば強調する
        tabTextList = tabTitleList,
        onSelected = {} // 特に何もしない
    ) {
        when (it) {
            VALID_TAB -> ValidRepellentTabContent(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = dimensionResource(R.dimen.home_screen_card_horizontal_padding),
                        end = dimensionResource(R.dimen.home_screen_card_horizontal_padding)
                    )
                    .fillMaxHeight(),
                validRepellentList = validRepellentList,
                currentDate = LocalDate.now(),
                resetOnClick = homeScreenViewModel::reset,
                cardOnClick = cardOnClick
            )

            EXPIRED_TAB ->
                ExpiredRepellentTabContent(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = dimensionResource(R.dimen.home_screen_card_horizontal_padding),
                            end = dimensionResource(R.dimen.home_screen_card_horizontal_padding)
                        )
                        .fillMaxHeight(),
                    expiredRepellentList = expiredRepellentList,
                    resetOnClick = homeScreenViewModel::reset,
                    cardOnClick = cardOnClick,
                    skipOnClick = homeScreenViewModel::skip
                )

            HISTORY_TAB -> // TODO 
                LoadingContent(
                    bottomPadding = dimensionResource(R.dimen.tab_item_height)
                )

            OTHERS_TAB -> // TODO 
                LoadingContent(
                    bottomPadding = dimensionResource(R.dimen.tab_item_height)
                )
        }
    }
}