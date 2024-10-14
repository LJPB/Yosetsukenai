package me.ljpb.yosetsukenai.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.InsectAction
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.common.ConfirmDialog
import me.ljpb.yosetsukenai.ui.components.common.RowItemWithOneItem
import me.ljpb.yosetsukenai.ui.components.common.RowItemWithText
import me.ljpb.yosetsukenai.ui.components.common.SimpleTextField
import me.ljpb.yosetsukenai.ui.components.edit.DatePickerModal
import me.ljpb.yosetsukenai.ui.components.edit.EditBottomBar
import me.ljpb.yosetsukenai.ui.components.edit.EditTopBar
import me.ljpb.yosetsukenai.ui.epochSecondToLocalDate
import me.ljpb.yosetsukenai.ui.getTextOfLocalDate
import me.ljpb.yosetsukenai.ui.localDateToEpochSecond
import java.time.LocalDate

private enum class InsectEditDialogType {
    DatePicker,
    Cancel,
    Delete,
    None // 初期値用
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsectEditContent(
    modifier: Modifier = Modifier,
    insectEditViewModel: InsectEditViewModel,
    isLandscape: Boolean,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    val name by insectEditViewModel.name.collectAsState()
    val date by insectEditViewModel.date.collectAsState()
    val size by insectEditViewModel.size.collectAsState()
    val insectCondition by insectEditViewModel.insectCondition.collectAsState()
    val place by insectEditViewModel.place.collectAsState()
    val zoneId by insectEditViewModel.zoneId.collectAsState()

    // ============ ダイアログ関連 ============
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var dialogType by rememberSaveable { mutableStateOf(InsectEditDialogType.None) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = localDateToEpochSecond(date, zoneId) * 1000
    )

    val showDialogOf = { type: InsectEditDialogType ->
        showDialog = true
        dialogType = type
    }
    val hiddenDialog = {
        showDialog = false
    }

    if (showDialog) {
        when (dialogType) {
            InsectEditDialogType.DatePicker -> {
                DatePickerModal(
                    datePickerState = datePickerState,
                    onDismiss = hiddenDialog,
                    onConfirm = { epochMillis ->
                        insectEditViewModel.setDate(
                            epochSecondToLocalDate(
                                epochMillis / 1000,
                                zoneId
                            )
                        )
                        hiddenDialog()
                    },
                    isLandscape = isLandscape
                )
            }

            InsectEditDialogType.Cancel -> {
                val bodyText: String
                val dismissButtonText: String
                val confirmButtonText: String
                if (insectEditViewModel.isUpdate) {
                    bodyText = stringResource(R.string.edit_cancel_text)
                    dismissButtonText = stringResource(R.string.edit_cancel_dismiss_button)
                    confirmButtonText = stringResource(R.string.edit_cancel_confirm_button)
                } else {
                    bodyText = stringResource(R.string.add_cancel_text)
                    dismissButtonText = stringResource(R.string.add_cancel_dismiss_button)
                    confirmButtonText = stringResource(R.string.add_cancel_confirm_button)
                }
                ConfirmDialog(
                    title = null,
                    body = bodyText,
                    dismissButtonText = dismissButtonText,
                    confirmButtonText = confirmButtonText,
                    onDismiss = { hiddenDialog() },
                    onConfirm = {
                        hiddenDialog()
                        onCancel()
                    }
                )
            }

            InsectEditDialogType.Delete -> {
                ConfirmDialog(
                    title = null,
                    body = stringResource(R.string.edit_delete_text),
                    dismissButtonText = stringResource(R.string.edit_delete_dismiss_button),
                    confirmButtonText = stringResource(R.string.edit_delete_confirm_button),
                    onDismiss = { hiddenDialog() },
                    onConfirm = {
                        hiddenDialog()
                        onDelete()
                    }
                )
            }

            else -> {}
        }
    }
    // ============ ダイアログ関連 終了 ============

    val textStyle = MaterialTheme.typography.titleMedium
    val textColor = MaterialTheme.colorScheme.onSurface

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // 端末の戻るボタンを押した時の処理
    BackHandler {
        if (insectEditViewModel.isChanged()) {
            showDialogOf(InsectEditDialogType.Cancel)
        } else {
            onCancel()
        }
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
                    if (insectEditViewModel.isChanged()) {
                        showDialogOf(InsectEditDialogType.Cancel)
                    } else {
                        onCancel()
                    }
                },
                onSave = {
                    onSaved()
                },
                enabled = name.isNotEmpty(), // nameが空白なら保存できない
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            if (insectEditViewModel.isUpdate) {
                EditBottomBar {
                    showDialogOf(InsectEditDialogType.Delete)
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
            // 発見した虫の名前
            RowItemWithOneItem(
                leadingIcon = ConstIcon.INSECT_NAME,
                itemName = stringResource(id = R.string.insect_name),
                item = {
                    SimpleTextField(
                        value = name,
                        onValueChange = insectEditViewModel::setName,
                        textStyle = textStyle,
                        textColor = textColor,
                        textAlign = TextAlign.End,
                        placeholderText = stringResource(id = R.string.edit_insect_name),
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
            HorizontalDivider()

            // 発見した日付
            RowItemWithText(
                leadingIcon = ConstIcon.INSECT_DATE,
                itemName = stringResource(id = R.string.insect_date),
                text = getTextOfLocalDate(date, context)
            ) { showDialogOf(InsectEditDialogType.DatePicker) }
            HorizontalDivider()

            // 発見した虫の大きさ
            RowItemWithOneItem(
                leadingIcon = ConstIcon.INSECT_SIZE,
                itemName = stringResource(id = R.string.insect_size),
                item = {
                    SimpleTextField(
                        value = size,
                        onValueChange = insectEditViewModel::setSize,
                        textStyle = textStyle,
                        textColor = textColor,
                        textAlign = TextAlign.End,
                        placeholderText = stringResource(id = R.string.edit_insect_size),
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
            HorizontalDivider()

            // 発見した虫の状態
            RowItemWithOneItem(
                leadingIcon = ConstIcon.INSECT_CONDITION,
                itemName = stringResource(id = R.string.insect_condition),
                item = {
                    SimpleTextField(
                        value = insectCondition,
                        onValueChange = insectEditViewModel::setInsectCondition,
                        textStyle = textStyle,
                        textColor = textColor,
                        textAlign = TextAlign.End,
                        placeholderText = stringResource(id = R.string.edit_insect_condition),
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
            HorizontalDivider()

            // 発見した虫の場所
            RowItemWithOneItem(
                leadingIcon = ConstIcon.INSECT_PLACE,
                itemName = stringResource(id = R.string.insect_place),
                item = {
                    SimpleTextField(
                        value = place,
                        onValueChange = insectEditViewModel::setPlace,
                        textStyle = textStyle,
                        textColor = textColor,
                        textAlign = TextAlign.End,
                        placeholderText = stringResource(id = R.string.edit_insect_place),
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
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsectEditContentPreview() {
    val viewModel = InsectEditViewModel(
        insect = null,
        insectAction = object : InsectAction {
            override suspend fun insert(entity: InsectEntity): Long {
                TODO("Not yet implemented")
            }

            override suspend fun update(entity: InsectEntity) {
                TODO("Not yet implemented")
            }

            override suspend fun delete(entity: InsectEntity) {
                TODO("Not yet implemented")
            }

            override fun getSize(): Long {
                TODO("Not yet implemented")
            }

            override fun getInsects(from: LocalDate, to: LocalDate): Flow<List<InsectEntity>> {
                TODO("Not yet implemented")
            }

            override fun getPagedInsects(limit: Int, offset: Int): Flow<List<InsectEntity>> {
                TODO("Not yet implemented")
            }
        }
    )
    InsectEditContent(
        modifier = Modifier,
        insectEditViewModel = viewModel,
        isLandscape = true,
        onSaved = {},
        onCancel = {},
        onDelete = {}
    )
}
