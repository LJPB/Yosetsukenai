package me.ljpb.yosetsukenai.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.home.ExpiredRepellentTabContent
import me.ljpb.yosetsukenai.ui.components.home.FloatingActionMenuItem
import me.ljpb.yosetsukenai.ui.components.home.TabContainer
import me.ljpb.yosetsukenai.ui.components.home.TabContent
import me.ljpb.yosetsukenai.ui.components.home.ValidRepellentTabContent

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    cardOnClick: (RepellentScheduleEntity) -> Unit,
    addInsectOnClick: () -> Unit,
    addRepellentOnClick: () -> Unit,
) {
    val validRepellentList by homeScreenViewModel.validRepellentList.collectAsState()
    val expiredRepellentList by homeScreenViewModel.expiredRepellentList.collectAsState()

    val validTabContent = TabContent(stringResource(R.string.valid_tab_title_text)) {
        ValidRepellentTabContent(
            modifier = Modifier.padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            validRepellentList = validRepellentList,
            currentDate = homeScreenViewModel.currentDate,
            resetOnClick = homeScreenViewModel::reset,
            cardOnClick = cardOnClick
        )
    }

    val expiredTabContent = TabContent(stringResource(R.string.expired_tab_title_text)) {
        ExpiredRepellentTabContent(
            modifier = Modifier.padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            expiredRepellentList = expiredRepellentList,
            skipOnClick = homeScreenViewModel::skip,
            resetOnClick = homeScreenViewModel::reset,
            cardOnClick = cardOnClick
        )
    }

    val historyTabContent = TabContent(stringResource(R.string.history_tab_title_text)) {
        Text(text = "未実装") // TODO 
    }

    val othersTabContent = TabContent(stringResource(R.string.others_tab_title_text)) {
        Text(text = "未実装") // TODO
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionMenu(
                addInsectOnClick = addInsectOnClick,
                addRepellentOnClick = addRepellentOnClick,
            )
        }
    ) { innerPadding ->
        TabContainer(
            modifier = Modifier.padding(innerPadding),
            // 対応が必要な虫除けがある場合は，最初にそれを表示する
            defaultIndex = if (expiredRepellentList?.isNotEmpty() == true) 1 else 0,
            // 対応が必要な虫除けがある場合は，わかりやすくエラーにする
            errorIndex = if (expiredRepellentList?.isNotEmpty() == true) 1 else -1,
            onSelected = { /* タブ切り替え時の動作。何もしない。 */},
            tabContent = listOf(
                validTabContent,
                expiredTabContent,
                historyTabContent,
                othersTabContent
            )
        )
    }
}

@Composable
private fun FloatingActionMenu(
    modifier: Modifier = Modifier,
    addRepellentOnClick: () -> Unit,
    addInsectOnClick: () -> Unit,
) {
    val addRepellentLabel = stringResource(R.string.fab_label_add_repellent)
    val addInsectLabel = stringResource(R.string.fab_label_add_insect)

    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    if (expanded) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { expanded = false } // FABを展開している時は画面をタップするとFABの展開を閉じる
                )
        ) {}
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility( // FABの展開/縮小に応じてアニメーションで表示/非表示する
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            FloatingActionMenuItem(
                icon = ConstIcon.FAB_ADD_INSECT,
                label = addInsectLabel,
                contentDescription = addInsectLabel,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = addInsectOnClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FloatingActionMenuItem(
            icon = ConstIcon.FAB_ADD_REPELLENT,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            label = if (expanded) addRepellentLabel else null, // 展開していない時はラベルを表示しない
            contentDescription = addRepellentLabel,
        ) {
            if (expanded) {
                addRepellentOnClick()
            } else {
                expanded = true // 展開していない時にクリックされたら展開する
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FloatingActionMenuPreview(modifier: Modifier = Modifier) {
    FloatingActionMenu(
        addInsectOnClick = {},
        addRepellentOnClick = {},
    )
}