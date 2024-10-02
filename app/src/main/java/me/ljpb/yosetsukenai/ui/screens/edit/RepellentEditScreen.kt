package me.ljpb.yosetsukenai.ui.screens.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RepellentEditScreen(
    modifier: Modifier = Modifier,
    repellentEditViewModel: RepellentEditViewModel,
    isLandscape: Boolean,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    RepellentEditContent(
        modifier = modifier,
        repellentEditViewModel = repellentEditViewModel,
        isLandscape = isLandscape,
        onSaved = onSaved,
        onCancel = onCancel,
        onDelete = onDelete
    )
}