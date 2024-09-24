package me.ljpb.yosetsukenai.ui.components.edit

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.ConstIcon

/**
 * @param enabled 保存ボタンを押せる?
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopBar(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    enabled: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        modifier = modifier,
        title = {}, // タイトルは表示しない
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = ConstIcon.CANCEL,
                    contentDescription = stringResource(id = R.string.cancel)
                )
            }
        },
        actions = {
            Button(
                onClick = onSave,
                enabled = enabled
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun EditBottomBar(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = Color.Transparent
    ) {
        TextButton(onClick = onDelete) {
            Text(
                text = stringResource(id = R.string.delete),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EditTopBarPreview() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    EditTopBar(onCancel = {}, onSave = {}, scrollBehavior = scrollBehavior)
}

@Preview
@Composable
private fun EditBottomBarPreview() {
    EditBottomBar{}
}