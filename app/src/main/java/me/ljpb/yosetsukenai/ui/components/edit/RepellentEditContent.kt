package me.ljpb.yosetsukenai.ui.components.edit

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.RowItem
import me.ljpb.yosetsukenai.ui.components.RowItemWithOneItem
import me.ljpb.yosetsukenai.ui.components.RowItemWithText
import me.ljpb.yosetsukenai.ui.components.SimpleTextField
import me.ljpb.yosetsukenai.ui.getTextOfLocalDate
import me.ljpb.yosetsukenai.ui.getTextOfNotify
import java.time.LocalDate

@Composable
fun RepellentEditContent(
    modifier: Modifier = Modifier,
    repellent: RepellentScheduleEntity?,
    notifies: List<NotifyEntity> = emptyList(),
) {
    // 表示するrepellentのプロパティの初期化
    // 渡されたrepellentがnullの場合 : 新規追加だから初期値で表示
    // 渡されたrepellentが非nullの場合 : 編集だから渡されたrepellentの値を初期値として表示
    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var validityPeriod by remember { mutableStateOf(SimplePeriod.ofDays(30)) }
    var places by remember { mutableStateOf(emptyList<String>()) }

    if (repellent != null) {
        name = repellent.name
        startDate = repellent.startDate
        validityPeriod = repellent.validityPeriod
        places = repellent.places
    }

    val notifyList by remember { mutableStateOf(notifies) }

    // 順番に注意：validityPeriodの初期化後に書く
    var validityNumber by remember { mutableIntStateOf(validityPeriod.number) }
    var validityPeriodUnit by remember { mutableStateOf(validityPeriod.periodUnit) }

    val textStyle = MaterialTheme.typography.titleMedium
    val textColor = MaterialTheme.colorScheme.onSurface
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .clickable( // TextFieldの外をタップした時にフォーカスを外すためのもの
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.detail_content_vertical_padding))
    ) {
        // 商品名
        HorizontalDivider()
        RowItemWithOneItem(
            leadingIcon = ConstIcon.PRODUCT_NAME,
            itemName = stringResource(id = R.string.repellent_name),
            item = {
                SimpleTextField(
                    value = name,
                    onValueChange = { name = it },
                    textStyle = textStyle,
                    textColor = textColor,
                    textAlign = TextAlign.End,
                    placeholderText = stringResource(id = R.string.edit_repellent_name),
                    placeholderTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )
            }
        )

        // 開始日
        HorizontalDivider()
        RowItemWithText(
            leadingIcon = ConstIcon.START_DATE,
            itemName = stringResource(id = R.string.repellent_start_date),
            text = getTextOfLocalDate(startDate, context)
        ) { /* TODO 日付入力ダイアログの表示 */ }

        // 有効期間
        HorizontalDivider()
        RowItemWithOneItem(
            leadingIcon = ConstIcon.VALIDITY_PERIOD,
            itemName = stringResource(id = R.string.repellent_validity_period),
            item = {
                SimplePeriodInputField(
                    defaultValue = validityPeriod.copy(),
                    onNumberChanged = { validityNumber = it },
                    onPeriodUniteChanged = { validityPeriodUnit = it },
                    textStyle = textStyle,
                    textColor = textColor
                )
            }
        )

        // 場所
        HorizontalDivider()
        RowItem(
            leadingIcon = ConstIcon.PLACE,
            itemName = stringResource(id = R.string.repellent_place),
            item = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    places.forEach { place ->
                        ItemTag(
                            text = place,
                            deleteOnClick = {
                                /* TODO 削除処理を書く */
                            }
                        )
                    }
                    // 追加ボタン
                    TextButton(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
                        onClick = { /* TODO 追加ダイアログの表示 */ }
                    ) {
                        Text(
                            text = stringResource(id = R.string.add),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )

        // 通知
        HorizontalDivider()
        RowItem(
            leadingIcon = ConstIcon.NOTIFY,
            itemName = stringResource(id = R.string.repellent_notify),
            item = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    notifyList.forEach { notify ->
                        ItemTag(
                            text = getTextOfNotify(notify, context),
                            deleteOnClick = { /* TODO 削除処理を書く */ }
                        )
                    }
                    // 追加ボタン
                    TextButton(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
                        onClick = { /* TODO 追加ダイアログの表示 */ }
                    ) {
                        Text(
                            text = stringResource(id = R.string.add),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    } // Column
} // function

@Composable
private fun SimplePeriodInputField(
    modifier: Modifier = Modifier,
    defaultValue: SimplePeriod,
    onNumberChanged: (Int) -> Unit,
    onPeriodUniteChanged: (PeriodUnit) -> Unit,
    textStyle: TextStyle,
    textColor: Color
) {
    var numberText by remember { mutableStateOf(defaultValue.number.toString()) }
    var periodUnit by remember { mutableStateOf(defaultValue.periodUnit) }

    // TextFieldのwidthを3文字分確保する
    val measurer = rememberTextMeasurer()
    val measureResult = measurer.measure(
        text = "123",
        maxLines = 1,
    )

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var textFieldIsFocused by remember { mutableStateOf(true) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleTextField(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.row_item_height))
                .width(measureResult.size.width.dp)
                .border(
                    width = 2.dp,
                    color = if (textFieldIsFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(4.dp)
                )
                .onFocusChanged { state ->
                    textFieldIsFocused = state.isFocused
                    if (!state.isFocused) { // フォーカスが外れた時
                        if (numberText == "") {
                            // フォーカスが外れた時に何も入力されていなければ，初期値をセットする
                            numberText = defaultValue.number.toString()
                            onNumberChanged(defaultValue.number)
                        }
                    }
                },
            value = numberText,
            onValueChange = {
                if (it.isEmpty()) {
                    numberText = it
                }
                if (it.isNotEmpty() && it.isDigitsOnly() && it.length <= 3) {
                    numberText = it.toInt().toString()
                    onNumberChanged(it.toInt())
                }
            },
            textStyle = textStyle,
            textColor = textColor,
            textAlign = TextAlign.Center,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            placeholderText = "",
            placeholderTextColor = textColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextDropDownMenu(
            modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
            value = periodUnit.toText(context),
            onValueChanged = {
                periodUnit = it.toPeriodUnit(context)
                onPeriodUniteChanged(periodUnit)
            },
            items = listOf(
                stringResource(id = R.string.validity_period_day_unit),
                stringResource(id = R.string.validity_period_week_unit),
                stringResource(id = R.string.validity_period_month_unit),
                stringResource(id = R.string.validity_period_year_unit),
            ),
            textStyle = textStyle,
            textColor = textColor,
        )
    }
}

@Composable
private fun TextDropDownMenu(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    items: List<String>,
    textStyle: TextStyle,
    textColor: Color
) {
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.clickable {
            focusManager.clearFocus()
            expanded = true
        },
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = value,
                style = textStyle,
                color = textColor
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = textStyle,
                            color = textColor
                        )
                    },
                    onClick = {
                        onValueChanged(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun PeriodUnit.toText(context: Context): String =
    when (this) {
        PeriodUnit.Day -> context.getString(R.string.validity_period_day_unit)
        PeriodUnit.Week -> context.getString(R.string.validity_period_week_unit)
        PeriodUnit.Month -> context.getString(R.string.validity_period_month_unit)
        PeriodUnit.Year -> context.getString(R.string.validity_period_year_unit)
    }

private fun String.toPeriodUnit(context: Context): PeriodUnit =
    when (this) {
        context.getString(R.string.validity_period_day_unit) -> PeriodUnit.Day
        context.getString(R.string.validity_period_week_unit) -> PeriodUnit.Week
        context.getString(R.string.validity_period_month_unit) -> PeriodUnit.Month
        else -> PeriodUnit.Year
    }

@Composable
private fun ItemTag(
    modifier: Modifier = Modifier,
    text: String,
    deleteOnClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End
            )
        }
        IconButton(
            onClick = deleteOnClick
        ) {
            Icon(
                imageVector = ConstIcon.CLOSE,
                contentDescription = stringResource(id = R.string.delete),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextDropMenuPreview() {
    TextDropDownMenu(
        value = "aaa",
        onValueChanged = {},
        items = listOf("a", "b", "c"),
        textStyle = MaterialTheme.typography.titleMedium,
        textColor = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview(showBackground = true)
@Composable
private fun SimplePeriodInputFieldPreview() {
    val simplePeriod by remember { mutableStateOf(SimplePeriod.ofDays(3)) }
    SimplePeriodInputField(
        defaultValue = SimplePeriod.ofDays(3),
        onNumberChanged = { simplePeriod.number = it },
        onPeriodUniteChanged = { simplePeriod.periodUnit = it },
        textStyle = MaterialTheme.typography.titleMedium,
        textColor = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview(showBackground = true)
@Composable
private fun ItemTagPreview() {
    ItemTag(
        text = "aaa",
    )
}

@Preview(showBackground = true)
@Composable
private fun RepellentEditPreview() {
    RepellentEditContent(
        repellent = null
    )
}