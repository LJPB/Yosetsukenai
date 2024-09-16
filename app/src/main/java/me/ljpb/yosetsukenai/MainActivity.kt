package me.ljpb.yosetsukenai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ljpb.yosetsukenai.ui.ViewModelProvider
import me.ljpb.yosetsukenai.ui.components.edit.RepellentEditContent
import me.ljpb.yosetsukenai.ui.RepellentEditViewModel
import me.ljpb.yosetsukenai.ui.theme.YosetsukenaiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YosetsukenaiTheme {
                val viewModel: RepellentEditViewModel = viewModel(factory = ViewModelProvider.repellentEditViewModel(null, listOf()))
                RepellentEditContent(
                    repellentEditViewModel = viewModel,
                    isLandscape = true
                ){}
            }
        }
    }
}