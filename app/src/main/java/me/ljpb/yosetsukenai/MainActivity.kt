package me.ljpb.yosetsukenai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ljpb.yosetsukenai.notification.AppNotificationManager
import me.ljpb.yosetsukenai.ui.ViewModelProvider
import me.ljpb.yosetsukenai.ui.YosetsukenaiApp
import me.ljpb.yosetsukenai.ui.theme.YosetsukenaiTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppNotificationManager.createNotificationChannel(this)
        setContent {
            YosetsukenaiTheme {
                YosetsukenaiApp(
                    homeScreenViewModel = viewModel(
                        factory = ViewModelProvider.homeScreenViewModel(LocalDate.now())
                    )
                )
            }
        }
    }
}