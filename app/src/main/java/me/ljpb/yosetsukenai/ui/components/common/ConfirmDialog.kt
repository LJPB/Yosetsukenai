package me.ljpb.yosetsukenai.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
    title: String?,
    body: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissButtonText)
            }
        },
        title = if (title != null) {
            {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            null
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    ConfirmDialog(
        title = "title",
        body = "body",
        dismissButtonText = "cancel",
        confirmButtonText = "ok",
        onConfirm = {},
        onDismiss = {}
    )
}