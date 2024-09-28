package me.ljpb.yosetsukenai.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R

/**
 * @param errorIndex 指定したインデックス値のタブテキストをError状態にする。 -1ならどれもエラーにしない。
 */
@Composable
fun TabContainer(
    modifier: Modifier = Modifier,
    defaultIndex: Int,
    errorIndex: Int,
    onSelected: (Int) -> Unit,
    tabContent: List<TabContent>
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(defaultIndex) }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                Box( // タブ選択時の下線に丸みを持たせている
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp)
                        .padding(start = 16.dp, end = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 4.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                )
            }
        ) {
            tabContent.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.tab_item_height)),
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        onSelected(index)
                    }
                ) {
                    Text(
                        text = item.title,
                        color = if (index == errorIndex) {
                            MaterialTheme.colorScheme.error
                        } else if (selectedTabIndex == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
        tabContent[selectedTabIndex].content()
    }
}

@Preview
@Composable
private fun TabContainerPreview() {
    TabContainer(
        onSelected = {},
        errorIndex = 2,
        defaultIndex = 2,
        tabContent = listOf(
            TabContent("aaa", {}),
            TabContent("bbb", {}),
            TabContent("ccc", {}),
        )
    )
}