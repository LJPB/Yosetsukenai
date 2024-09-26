package me.ljpb.yosetsukenai.ui.screens.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RepellentEditScreen(
    modifier: Modifier = Modifier,
    repellentEditViewModel: RepellentEditViewModel,
    isLandscape: Boolean,
    onCancel: () -> Unit,
) {
    // TODO: 諸々の処理 
    RepellentEditContent(
        modifier = modifier,
        repellentEditViewModel = repellentEditViewModel,
        isLandscape = isLandscape,
        onCancel = onCancel
    )
}