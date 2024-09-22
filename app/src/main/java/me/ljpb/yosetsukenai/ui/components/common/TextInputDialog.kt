package me.ljpb.yosetsukenai.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R

/**
 * 文字列を入力するためのダイアログ
 * @param allowEmpty 何も入力されていない時にconfirmButtonを押せるかどうか(空文字を許容するかどうか)
 */
@Composable
fun TextInputDialog(
    modifier: Modifier = Modifier,
    defaultValue: String,
    label: String,
    allowEmpty: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onSave: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var text by rememberSaveable { mutableStateOf(defaultValue) }

    // isErrorがtrueのとき，入力テキストが変更されたらエラー状態を解除したい。そのためのテキストの変更状態を管理するフラグ。
    // isErrorがfalseのときは，常にfalse
    // isErrorがtrueでかつ，そのときにTextFieldの値が更新されたらtrueになる
    var isValueChangedOnError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = if (allowEmpty) true else text.isNotEmpty(), // allowEmptyがfalseのとき，TextFieldが空ならボタンを押せない
                onClick = {
                    onSave(text)
                    // 保存ボタンを押すと状態をリセットする
                    // もしisValueChangedOnError = trueのままだと，isErrorがtrueとなってもエラー状態が直ちに解除されてしまう
                    isValueChangedOnError = false
                }
            ) {
                Text(text = stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        text = {
            Column {
                // エラーメッセージ。エラー状態で入力テキストが未更新の場合に表示する(更新後は非表示になる)。
                if (errorMessage != null && isError && !isValueChangedOnError) {
                    Text(
                        modifier = Modifier.padding(vertical = 2.dp),
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        // isErrorがtrueのときに，入力テキストが変更されたらtrueにしたい。それ以外の時はfalseにしたい
                        // このメソッドは入力テキストが更新されたときに呼ばれるため，isErrorを渡せば期待の値をセットできる
                        isValueChangedOnError = isError
                    },
                    label = { Text(text = label) },
                    singleLine = true,
                    // エラー状態で入力テキストが未更新の場合にエラーとなる
                    isError = isError && !isValueChangedOnError
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