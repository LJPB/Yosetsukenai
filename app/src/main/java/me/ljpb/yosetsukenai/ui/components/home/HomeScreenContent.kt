package me.ljpb.yosetsukenai.ui.components.home

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate

private const val VALID_TAB = 0
private const val EXPIRED_TAB = 1
private const val HISTORY_TAB = 2
private const val OTHERS_TAB = 3

/**
 * @param resetOnClick 同じ内容の虫除けを再設定するボタンを押した時の処理
 * @param skipOnClick 再設定せずに期限切れリストから外すための処理
 */
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    validRepellentList: List<RepellentScheduleEntity>?,
    expiredRepellentList: List<RepellentScheduleEntity>?,
    resetOnClick: (RepellentScheduleEntity) -> Unit,
    skipOnClick: (RepellentScheduleEntity) -> Unit,
    cardOnClick: (RepellentScheduleEntity) -> Unit,
) {
    val tabTitleList = listOf(
        stringResource(R.string.valid_tab_title_text),
        stringResource(R.string.expired_tab_title_text),
        stringResource(R.string.history_tab_title_text),
        stringResource(R.string.others_tab_title_text),
    )
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
                resetOnClick = resetOnClick,
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
                    resetOnClick = resetOnClick,
                    cardOnClick = cardOnClick,
                    skipOnClick = skipOnClick
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