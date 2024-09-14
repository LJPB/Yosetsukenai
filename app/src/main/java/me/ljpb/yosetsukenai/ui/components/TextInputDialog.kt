package me.ljpb.yosetsukenai.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R

/**
 * @param allowEmpty 何も入力されていない時にconfirmButtonを押せるかどうか(空文字を許容するかどうか)
 */
@Composable
fun TextInputDialog(
    modifier: Modifier = Modifier,
    defaultValue: String,
    label: String,
    allowEmpty: Boolean = false,
    onSave: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var text by remember { mutableStateOf(defaultValue) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = if (allowEmpty) true else text.isNotEmpty(), // allowEmptyがfalseのとき，TextFieldが空ならボタンを押せない
                onClick = { onSave(text) }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    label = { Text(text = label) },
                    singleLine = true,
                )
            }
        }
    )
}

@Preview
@Composable
private fun TextInputDialogPreview() {
    TextInputDialog(
        defaultValue = "",
        label = "aaa",
    )
}