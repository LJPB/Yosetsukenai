package me.ljpb.yosetsukenai.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.ViewModelProvider.repellentDetailViewModel
import me.ljpb.yosetsukenai.ui.ViewModelProvider.repellentEditViewModel
import me.ljpb.yosetsukenai.ui.screens.detail.RepellentDetailScreen
import me.ljpb.yosetsukenai.ui.screens.detail.RepellentDetailViewModel
import me.ljpb.yosetsukenai.ui.screens.edit.RepellentEditScreen
import me.ljpb.yosetsukenai.ui.screens.edit.RepellentEditViewModel
import me.ljpb.yosetsukenai.ui.screens.home.HomeScreen
import me.ljpb.yosetsukenai.ui.screens.home.HomeScreenViewModel

enum class AppScreen {
    Home,
    DetailRepellent,
    EditRepellent,
    AddRepellent,
    AddInsect
}

@Composable
fun YosetsukenaiApp(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
) {
    val navController = rememberNavController()
    // 詳細画面で表示する虫除け
    lateinit var selectedDetailRepellent: RepellentScheduleEntity
    // 編集画面で編集(表示)する虫除け
    lateinit var selectedEditRepellent: RepellentScheduleEntity
    // 編集画面で編集(表示)する通知リスト
    lateinit var selectedEditNotifications: List<NotificationEntity>

    lateinit var repellentDetailViewModel: RepellentDetailViewModel
    lateinit var repellentEditViewModel: RepellentEditViewModel
    Surface {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = AppScreen.Home.name
        ) {
            composable(route = AppScreen.Home.name) {
                HomeScreen(
                    homeScreenViewModel = homeScreenViewModel,
                    addRepellentOnClick = {
                        navController.navigate(AppScreen.AddRepellent.name)
                    },
                    addInsectOnClick = {},
                    cardOnClick = {
                        selectedDetailRepellent = it
                        navController.navigate(AppScreen.DetailRepellent.name)
                    }
                )
            }
            composable(route = AppScreen.DetailRepellent.name) { // 詳細画面
                repellentDetailViewModel =
                    viewModel(factory = repellentDetailViewModel(selectedDetailRepellent))
                RepellentDetailScreen(
                    repellentDetailViewModel = repellentDetailViewModel,
                    insectOnClick = {},
                    backButtonOnClick = { navController.popBackStack(AppScreen.Home.name, false) },
                    editButtonOnClick = { repellent, notifications ->
                        selectedEditRepellent = repellent
                        selectedEditNotifications = notifications
                        navController.navigate(AppScreen.EditRepellent.name)
                    }
                )
            }
            composable(route = AppScreen.EditRepellent.name) { // 編集画面
                repellentEditViewModel = viewModel(
                    factory = repellentEditViewModel(
                        selectedEditRepellent,
                        selectedEditNotifications
                    )
                )
                RepellentEditScreen(
                    repellentEditViewModel = repellentEditViewModel,
                    isLandscape = true,
                    onSaved = { navController.popBackStack(AppScreen.Home.name, false) },
                    onCancel = { navController.popBackStack(AppScreen.DetailRepellent.name, false) }
                )
            }
            composable(route = AppScreen.AddRepellent.name) { // 新規追加画面
                repellentEditViewModel = viewModel(
                    factory = repellentEditViewModel(
                        null,
                        emptyList()
                    )
                )
                RepellentEditScreen(
                    repellentEditViewModel = repellentEditViewModel,
                    isLandscape = true,
                    onSaved = { navController.popBackStack(AppScreen.Home.name, false) },
                    onCancel = { navController.popBackStack(AppScreen.Home.name, false) }
                )
            }
        }
    }
}