package me.ljpb.yosetsukenai.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    appViewModel: AppViewModel = viewModel(),
    homeScreenViewModel: HomeScreenViewModel,
) {
    val navController = rememberNavController()

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
                        appViewModel.navigateToRepellentAddScreen {
                            navController.navigate(AppScreen.AddRepellent.name)
                        }
                    },
                    addInsectOnClick = {},
                    cardOnClick = {
                        appViewModel.navigateToRepellentDetailScreen(it) {
                            navController.navigate(AppScreen.DetailRepellent.name)
                        }
                    }
                )
            }

            composable(route = AppScreen.DetailRepellent.name) { // 詳細画面
                repellentDetailViewModel =
                    viewModel(factory = appViewModel.getFactoryOfRepellentDetailViewModel())
                RepellentDetailScreen(
                    repellentDetailViewModel = repellentDetailViewModel,
                    insectOnClick = {},
                    backButtonOnClick = { navController.popBackStack(AppScreen.Home.name, false) },
                    editButtonOnClick = { repellent, notifications ->
                        appViewModel.navigateToRepellentEditScreen(
                            repellent,
                            notifications
                        ) {
                            navController.navigate(AppScreen.EditRepellent.name)
                        }
                    }
                )
            }

            composable(route = AppScreen.EditRepellent.name) { // 編集画面
                repellentEditViewModel =
                    viewModel(factory = appViewModel.getFactoryOfRepellentEditViewModel())
                RepellentEditScreen(
                    repellentEditViewModel = repellentEditViewModel,
                    isLandscape = true,
                    onSaved = {
                        // 上書き保存のときはホーム画面に戻る
                        repellentEditViewModel.save()
                        navController.popBackStack(AppScreen.Home.name, false)
                    },
                    onCancel = {
                        // 編集をキャンセルした場合は，元の詳細画面に戻る
                        navController.popBackStack(AppScreen.DetailRepellent.name, false)
                    },
                    onDelete = {
                        // 削除した場合はホーム画面に戻る
                        repellentEditViewModel.delete()
                        navController.popBackStack(AppScreen.Home.name, false)
                    }
                )
            }

            composable(route = AppScreen.AddRepellent.name) { // 新規追加画面
                repellentEditViewModel =
                    viewModel(factory = appViewModel.getFactoryOfRepellentAddViewModel())
                val closeScreen = { navController.popBackStack(AppScreen.Home.name, false) }
                RepellentEditScreen(
                    repellentEditViewModel = repellentEditViewModel,
                    isLandscape = true,
                    onSaved = {
                        repellentEditViewModel.save()
                        closeScreen()
                    },
                    onCancel = {
                        closeScreen()
                    },
                    onDelete = {
                        closeScreen()
                    }
                )
            }
        }
    }
}