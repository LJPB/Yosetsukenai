package me.ljpb.yosetsukenai

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import me.ljpb.yosetsukenai.notification.AppNotificationManager.createNotificationChannel
import me.ljpb.yosetsukenai.ui.ViewModelProvider
import me.ljpb.yosetsukenai.ui.YosetsukenaiApp
import me.ljpb.yosetsukenai.ui.screens.PermissionScreen
import me.ljpb.yosetsukenai.ui.theme.YosetsukenaiTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YosetsukenaiTheme {
                if (Build.VERSION.SDK_INT >= 33) {
                    var showPermissionScreen by rememberSaveable { mutableStateOf(true) }
                    val permissionState =
                        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
                    if (permissionState.status.isGranted) {
                        createNotificationChannel(this)
                        showPermissionScreen = false
                    } else {
                        if (showPermissionScreen) {
                            PermissionScreen(permissionState) {
                                showPermissionScreen = false
                            }
                        }
                    }
                } else {
                    createNotificationChannel(this)
                }
                YosetsukenaiApp(
                    homeScreenViewModel = viewModel(
                        factory = ViewModelProvider.homeScreenViewModel(LocalDate.now())
                    )
                )
            }
        }
    }
}
