package me.ljpb.yosetsukenai.ui.components.edit

import android.content.Context
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.ViewModelProvider
import me.ljpb.yosetsukenai.ui.components.common.NotifyInputDialog
import me.ljpb.yosetsukenai.ui.components.common.RowItem
import me.ljpb.yosetsukenai.ui.components.common.RowItemWithOneItem
import me.ljpb.yosetsukenai.ui.components.common.RowItemWithText
import me.ljpb.yosetsukenai.ui.components.common.SimpleTextField
import me.ljpb.yosetsukenai.ui.components.common.TextInputDialog
import me.ljpb.yosetsukenai.ui.epochSecondToLocalDate
import me.ljpb.yosetsukenai.ui.getNotifyText
import me.ljpb.yosetsukenai.ui.getTextOfLocalDate
import me.ljpb.yosetsukenai.ui.localDateToEpochSecond

private enum class DialogType {
    DatePicker,
    Place,
    Notify,
    None // 初期値用
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepellentEditContent(
    modifier: Modifier = Modifier,
    repellentEditViewModel: RepellentEditViewModel,
    onCancel: () -> Unit,
) {
    val name by repellentEditViewModel.name.collectAsState()
    val startDate by repellentEditViewModel.startDate.collectAsState()
    val validityPeriodUnit by repellentEditViewModel.validityPeriodUnit.collectAsState()
    val validityNumber by repellentEditViewModel.validityNumber.collectAsState()
    val zoneId by repellentEditViewModel.zoneId.collectAsState()
    val places by repellentEditViewModel.places.collectAsState()
    val notifyList by repellentEditViewModel.notifyList.collectAsState()

    val textStyle = MaterialTheme.typography.titleMedium
    val textColor = MaterialTheme.colorScheme.onSurface
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // ============ ダイアログ関連 ============
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var dialogType by rememberSaveable { mutableStateOf(DialogType.None) }

    val showDialogOf = { type: DialogType ->
        showDialog = true
        dialogType = type
    }
    val hiddenDialog = {
        showDialog = false
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = localDateToEpochSecond(startDate, zoneId) * 1000
    )

    if (showDialog) {
        when (dialogType) {
            DialogType.DatePicker -> DatePickerModal(
                datePickerState = datePickerState,
                onDismiss = hiddenDialog,
                onConfirm = { epochMillis ->
                    repellentEditViewModel.setStartDate(
                        epochSecondToLocalDate(
                            epochMillis / 1000,
                            zoneId
                        )
                    )
                    hiddenDialog()
                },
                isLandscape = false
            )

            DialogType.Place -> TextInputDialog(
                defaultValue = "",
                label = stringResource(id = R.string.edit_place),
                onSave = { text ->
                    repellentEditViewModel.addPlace(text)
                    hiddenDialog()
                },
                onDismiss = hiddenDialog,
                allowEmpty = false
            )

            DialogType.Notify -> NotifyInputDialog(
                onSave = { simplePeriod, simpleTime ->
                    val pair = PeriodAndTime(simplePeriod, simpleTime)
                    repellentEditViewModel.addNotify(pair)
                    hiddenDialog()
                },
                onDismiss = hiddenDialog,
                isLandscape = false
            )

            else -> {}
        }
    }
    // ============ ダイアログ関連 終了 ============

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val canSave by repellentEditViewModel.canSave.collectAsState()

    // 端末の戻るボタンを押した時の処理
    BackHandler {
        // TODO: 変更済みの場合は確認ダイアログの表示
        onCancel()
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .clickable( // TextFieldの外をタップした時にフォーカスを外すためのもの
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            EditTopBar(
                onCancel = {
                    // TODO: 変更済みの場合は確認ダイアログの表示 
                    onCancel()
                },
                onSave = repellentEditViewModel::saveRepellent,
                enabled = canSave,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            if (repellentEditViewModel.isUpdate) {
                EditBottomBar {
                    // TODO: 確認ダイアログの表示 
                    repellentEditViewModel.deleteRepellent()
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.detail_content_vertical_padding))
        ) {
            // 商品名
            RowItemWithOneItem(
                leadingIcon = ConstIcon.PRODUCT_NAME,
                itemName = stringResource(id = R.string.repellent_name),
                item = {
                    SimpleTextField(
                        value = name,
                        onValueChange = repellentEditViewModel::setName,
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
            ) { showDialogOf(DialogType.DatePicker) }

            // 有効期間
            HorizontalDivider()
            RowItemWithOneItem(
                leadingIcon = ConstIcon.VALIDITY_PERIOD,
                itemName = stringResource(id = R.string.repellent_validity_period),
                item = {
                    SimplePeriodInputField(
                        defaultValue = SimplePeriod.of(validityNumber, validityPeriodUnit),
                        onNumberChanged = repellentEditViewModel::setValidityNumber,
                        onPeriodUniteChanged = repellentEditViewModel::setValidityPeriodUnit,
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
                        places.forEachIndexed { index, place ->
                            ItemTag(
                                text = place,
                                deleteOnClick = { repellentEditViewModel.removePlace(index, place) }
                            )
                        }
                        // 追加ボタン
                        TextButton(
                            modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
                            onClick = { showDialogOf(DialogType.Place) }
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
                        notifyList.forEachIndexed { index, periodAndTime ->
                            ItemTag(
                                text = getNotifyText(
                                    periodAndTime.period,
                                    periodAndTime.time,
                                    context
                                ),
                                deleteOnClick = {
                                    repellentEditViewModel.removeNotify(
                                        index,
                                        periodAndTime
                                    )
                                }
                            )
                        }
                        // 追加ボタン
                        TextButton(
                            modifier = Modifier.height(dimensionResource(id = R.dimen.row_item_height)),
                            onClick = { showDialogOf(DialogType.Notify) }
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
    } // Scaffold
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
            value = if (numberText == RepellentEditViewModel.EMPTY_INT.toString()) "" else numberText,
            onValueChange = {
                if (it.isEmpty()) {
                    numberText = it
                    onNumberChanged(RepellentEditViewModel.EMPTY_INT)
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
    val viewModel: RepellentEditViewModel =
        viewModel(factory = ViewModelProvider.repellentEditViewModel(null, listOf()))
    RepellentEditContent(
        repellentEditViewModel = viewModel
    ) {}
}