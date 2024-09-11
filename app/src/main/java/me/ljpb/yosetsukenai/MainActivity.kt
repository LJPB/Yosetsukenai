package me.ljpb.yosetsukenai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.ui.components.ValidRepellentCard
import me.ljpb.yosetsukenai.ui.theme.YosetsukenaiTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YosetsukenaiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        ValidRepellentCard(
                            modifier = Modifier.padding(16.dp),
                            startDate = LocalDate.of(2024, 9, 1),
                            endDate = LocalDate.of(2024, 9, 3),
                            currentDate = LocalDate.of(2024, 9, 3),
                            name = "商品名",
                            validityPeriodText = "30日間",
                            places = listOf("場所1", "場所2", "場所3", "a", "aaaaaaaaaaa", "bbbbbbbb", "cccc", "d", "eeeeeeeeeeeeeeeeeeeeeeeee"),
                            resetOnClick = { /*TODO*/ }) {
                        }
                    }
                }
            }
        }
    }
}