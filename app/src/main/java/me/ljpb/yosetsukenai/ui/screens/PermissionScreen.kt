package me.ljpb.yosetsukenai.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.components.common.ConfirmDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    permissionState: PermissionState,
    hidden: () -> Unit,
) {
    ConfirmDialog(
        title = stringResource(R.string.notification_permission_title),
        body = stringResource(R.string.notification_permission_body),
        dismissButtonText = stringResource(R.string.notification_permission_negative),
        confirmButtonText = stringResource(R.string.notification_permission_positive),
        onDismiss = hidden,
        onConfirm = {
            hidden()
            permissionState.launchPermissionRequest()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionScreenPreview() {
    //    PermissionScreen(
    //        modifier = Modifier,
    //    )
}
