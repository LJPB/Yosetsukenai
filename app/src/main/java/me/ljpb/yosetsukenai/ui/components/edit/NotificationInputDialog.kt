package me.ljpb.yosetsukenai.ui.components.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime

private enum class NotifyDateUnit(val periodUnit: PeriodUnit) {
    Day(PeriodUnit.Day),
    Week(PeriodUnit.Week),
}

private const val EMPTY_INT_VALUE = -1

/**
 * 通知情報を入力するためのダイアログ
 * @param onSave(SimplePeriod, SimpleTime)
 * SimplePeriodは「何日前に通知を送るか」を表す期間
 * SimpleTimeは「何時に通知を送るか」を表す時間
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationInputDialog(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    onDismiss: () -> Unit = {},
    onSave: (SimplePeriod, SimpleTime) -> Unit,
    isLandscape: Boolean = false
) {
    val timePickerState = rememberTimePickerState()
    // 何日前/何週間前・・・などの日数/週数の部分
    var number by rememberSaveable { mutableIntStateOf(1) }
    // 何日前/何週間前・・・などの日/週間など単位の部分
    var dateUnit by rememberSaveable { mutableStateOf(NotifyDateUnit.Day) }

    // isErrorがtrueのとき，日時の値が変更されたらエラー状態を解除したい。そのためのテキストの変更状態を管理するフラグ。
    // isErrorがfalseのときは，常にfalse
    // isErrorがtrueでかつ，そのときに日時の値が更新されたらtrueになる
    var isValueChangedOnError by rememberSaveable { mutableStateOf(false) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        SimplePeriod.of(number, dateUnit.periodUnit),
                        SimpleTime.of(timePickerState.hour, timePickerState.minute)
                    )
                    // もしisValueChangedOnError = trueのままだと，isErrorがtrueとなってもエラー状態が直ちに解除されてしまう
                    isValueChangedOnError = false 
                },
                enabled = (number != EMPTY_INT_VALUE)
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
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                if (errorMessage != null && isError && !isValueChangedOnError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                DateSelector(
                    defaultNumber = 1,
                    defaultUnit = NotifyDateUnit.Day,
                    onNumberChanged = {
                        // isErrorがtrueのときに変更されたらtrueにしたい。それ以外の時はfalseにしたい
                        // このメソッドは入力テキストが更新されたときに呼ばれるため，isErrorを渡せば期待の値をセットできる
                        isValueChangedOnError = isError
                        number = it
                    },
                    onUnitChanged = {
                        // isErrorがtrueのときに変更されたらtrueにしたい。それ以外の時はfalseにしたい
                        // このメソッドは入力テキストが更新されたときに呼ばれるため，isErrorを渡せば期待の値をセットできる
                        isValueChangedOnError = isError
                        dateUnit = it
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                TimeInputContent(timePickerState = timePickerState)
                if (isLandscape) {
                    // 横画面ではダイアログのコンテントをスクロールすることを想定している
                    // そのためTimeInputContentがタップしやすい位置に来るようにスペースを追加している
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    )
    if (isError) { // timePickerStateが変わるたびに呼び出されないようにするために，エラー状態のときにのみ処理する
        LaunchedEffect(timePickerState.hour, timePickerState.minute) {
            // isErrorがtrueのときに変更されたらtrueにしたい。それ以外の時はfalseにしたい
            // このメソッドは入力テキストが更新されたときに呼ばれるため，isErrorを渡せば期待の値をセットできる
            isValueChangedOnError = true
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeInputContent(modifier: Modifier = Modifier, timePickerState: TimePickerState) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.notification_input_dialog_select_time),
            style = MaterialTheme.typography.labelLarge,
        )
        TimeInput(
            modifier = Modifier
                .scale(0.9f)
                .align(Alignment.CenterHorizontally),
            state = timePickerState
        )
    }
}

/**
 * @param onNumberChanged TextFieldに入力された値(数字文字列)をIntとして渡す。入力された値が空文字列の場合はEMPTY_INT_VALUEが渡される
 */
@Composable
private fun DateSelector(
    modifier: Modifier = Modifier,
    defaultNumber: Int,
    defaultUnit: NotifyDateUnit,
    onNumberChanged: (Int) -> Unit,
    onUnitChanged: (NotifyDateUnit) -> Unit
) {
    // NotifyDateUnit.ordinalでこのリストの要素にアクセスするため，要素はNotifyDateUnitの定義順でもれなく並べる必要がある
    val radioOptions = listOf(
        Pair(
            stringResource(id = R.string.notification_input_dialog_select_day),
            stringResource(id = R.string.notification_input_dialog_unit_day),
        ),
        Pair(
            stringResource(id = R.string.notification_input_dialog_select_week),
            stringResource(id = R.string.notification_input_dialog_unit_week),
        )
    )
    var numberText by rememberSaveable { mutableStateOf(defaultNumber.toString()) }
    var selectedUnit by rememberSaveable { mutableStateOf(defaultUnit) }

    val selectUnit = { dateUnit: NotifyDateUnit ->
        selectedUnit = dateUnit
        onUnitChanged(dateUnit)
    }

    Column(modifier = modifier) {
        Text(
            // タイトル的なやつ
            modifier = Modifier.padding(vertical = 8.dp),
            text = radioOptions[selectedUnit.ordinal].first,
            style = MaterialTheme.typography.labelLarge,
        )
        OutlinedTextField(
            // 何日前や何週間前といった数字の部分を入力する
            modifier = Modifier.padding(vertical = 4.dp),
            value = numberText,
            onValueChange = {
                if (it.isEmpty()) {
                    numberText = ""
                    onNumberChanged(EMPTY_INT_VALUE)
                } else if (it.isDigitsOnly() && it.length <= 3) {
                    val number = it.toInt()
                    numberText = number.toString()
                    onNumberChanged(number)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )

        // 日前や週間前といった単位を選択するラジオボタン
        NotifyDateUnit.entries.forEach { dateUnit ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedUnit == dateUnit),
                        onClick = { selectUnit(dateUnit) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedUnit == dateUnit),
                    onClick = { selectUnit(dateUnit) }
                )
                Text(
                    text = radioOptions[dateUnit.ordinal].second,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateSelectorPreview() {
    DateSelector(
        defaultNumber = 1,
        defaultUnit = NotifyDateUnit.Day,
        onNumberChanged = {},
        onUnitChanged = {},
    )
}

@Preview
@Composable
private fun NotificationInputDialogPreview() {
    NotificationInputDialog(
        onDismiss = {},
        onSave = { a, b -> },
        isLandscape = true,
        isError = true,
        errorMessage = "aaaaa"
    )
}