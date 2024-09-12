package me.ljpb.yosetsukenai.ui.components.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.ConstIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContainer(
    modifier: Modifier = Modifier,
    backButtonOnClick: () -> Unit,
    editButtonOnClick: () -> Unit,
    detailContent: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {}, // タイトルは表示しない
                // 戻るボタン
                navigationIcon = {
                    IconButton(onClick = backButtonOnClick) {
                        Icon(imageVector = ConstIcon.BACK, contentDescription = stringResource(id = R.string.back))
                    }
                },
                // 編集ボタン
                actions = {
                    TextButton(onClick = editButtonOnClick) {
                        Text(
                            text = stringResource(id = R.string.edit),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            detailContent()
        }
    }
}