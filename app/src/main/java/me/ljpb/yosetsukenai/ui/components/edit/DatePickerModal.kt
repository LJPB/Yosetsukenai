package me.ljpb.yosetsukenai.ui.components.edit

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R

/**
 * @param onConfirm 引数で渡す値はエポックミリ秒
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    isLandscape: Boolean
) {
    datePickerState.displayMode =
        if (isLandscape) DisplayMode.Input else datePickerState.displayMode
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(datePickerState.selectedDateMillis ?: System.currentTimeMillis()) },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(
                    text = stringResource(id = R.string.save)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = !isLandscape // 横画面なら表示しない
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DatePickerDialogContentPreview() {
    val state = rememberDatePickerState()
    DatePickerModal(
        datePickerState = state,
        onDismiss = {},
        onConfirm = {},
        isLandscape = false
    )
}