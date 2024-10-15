package me.ljpb.yosetsukenai.ui

import android.util.Log
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ljpb.yosetsukenai.ui.screens.detail.InsectDetailScreen
import me.ljpb.yosetsukenai.ui.screens.detail.RepellentDetailScreen
import me.ljpb.yosetsukenai.ui.screens.detail.RepellentDetailViewModel
import me.ljpb.yosetsukenai.ui.screens.edit.InsectEditScreen
import me.ljpb.yosetsukenai.ui.screens.edit.InsectEditViewModel
import me.ljpb.yosetsukenai.ui.screens.edit.RepellentEditScreen
import me.ljpb.yosetsukenai.ui.screens.edit.RepellentEditViewModel
import me.ljpb.yosetsukenai.ui.screens.home.HomeScreen
import me.ljpb.yosetsukenai.ui.screens.home.HomeScreenViewModel

enum class AppScreen {
    Home,
    DetailRepellent,
    DetailInsect,
    EditRepellent,
    AddRepellent,
    EditInsect,
    AddInsect,
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
    lateinit var insectEditViewModel: InsectEditViewModel

    Surface {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = AppScreen.Home.name
        ) {
            // ホーム画面
            composable(route = AppScreen.Home.name) {
                HomeScreen(
                    homeScreenViewModel = homeScreenViewModel,
                    addRepellentOnClick = { // 虫除けの追加画面への遷移
                        appViewModel.navigateToRepellentAddScreen {
                            navController.navigate(AppScreen.AddRepellent.name)
                        }
                    },
                    addInsectOnClick = { // 発見した虫の追加画面への遷移
                        navController.navigate(AppScreen.AddInsect.name)
                    },
                    cardOnClick = { // 虫除けの詳細画面への遷移
                        appViewModel.navigateToRepellentDetailScreen(it) {
                            navController.navigate(AppScreen.DetailRepellent.name)
                        }
                    }
                )
            }

            // 虫除けの詳細画面
            composable(route = AppScreen.DetailRepellent.name) {
                repellentDetailViewModel =
                    viewModel(factory = appViewModel.getFactoryOfRepellentDetailViewModel())

                RepellentDetailScreen(
                    repellentDetailViewModel = repellentDetailViewModel,
                    insectOnClick = { // 虫の詳細画面へ遷移
                        appViewModel.navigateToInsectDetailScreen(it) {
                            appViewModel.backRoute = AppScreen.DetailRepellent
                            navController.navigate(AppScreen.DetailInsect.name)
                        }
                    },
                    backButtonOnClick = { navController.popBackStack(AppScreen.Home.name, false) },
                    editButtonOnClick = { repellent, notifications ->  // 虫除けの編集画面への遷移
                        appViewModel.navigateToRepellentEditScreen(
                            repellent,
                            notifications
                        ) {
                            navController.navigate(AppScreen.EditRepellent.name)
                        }
                    }
                )
            }

            // 発見した虫の詳細画面
            composable(route = AppScreen.DetailInsect.name) {
                InsectDetailScreen(
                    insect = appViewModel.selectedDetailInsect,
                    backButtonOnClick = { navController.popBackStack() },
                    editButtonOnClick = { // 発見した虫の記録の編集画面へ遷移
                        appViewModel.navigateToInsectEditScreen(it) {
                            navController.navigate(AppScreen.EditInsect.name)
                        }
                    }
                )
            }

            // 発見した虫の編集画面
            // 虫の編集画面は詳細画面からしか遷移できないから、前の画面は常に詳細画面
            // 一方、前の前の画面は虫除けの詳細画面かもしれないし、記録の一覧画面かもしれない
            composable(route = AppScreen.EditInsect.name) {
                Log.d("backRoute", appViewModel.backRoute.name)
                insectEditViewModel =
                    viewModel(factory = appViewModel.getFactoryOfInsectEditViewModel())
                InsectEditScreen(
                    insectEditViewModel = insectEditViewModel,
                    isLandscape = true,
                    onSaved = { // 更新後に前の画面に戻る
                        insectEditViewModel.save()
                        navController.popBackStack(appViewModel.backRoute.name, false)
                    },
                    onDelete = {
                        insectEditViewModel.delete()
                        navController.popBackStack(appViewModel.backRoute.name, false)
                    },
                    onCancel = { // キャンセルした場合は元の画面に戻る
                        navController.popBackStack()
                    }
                )
            }

            // 虫の追加画面
            composable(route = AppScreen.AddInsect.name) {
                insectEditViewModel =
                    viewModel(factory = appViewModel.getFactoryOfInsectAddViewModel())
                val closeScreen =
                    { navController.popBackStack(AppScreen.Home.name, false) } // ホーム画面に戻る
                InsectEditScreen(
                    insectEditViewModel = insectEditViewModel,
                    isLandscape = true,
                    onSaved = { // 追加後はホーム画面に戻る
                        insectEditViewModel.save()
                        closeScreen()
                    },
                    onDelete = { closeScreen() },
                    onCancel = { closeScreen() } // キャンセルした場合は元の画面に戻る
                )
            }

            // 虫除けの編集画面
            composable(route = AppScreen.EditRepellent.name) {
                repellentEditViewModel =
                    viewModel(factory = appViewModel.getFactoryOfRepellentEditViewModel())

                RepellentEditScreen(
                    repellentEditViewModel = repellentEditViewModel,
                    isLandscape = true,
                    onSaved = { // 上書き保存のときはホーム画面に戻る
                        repellentEditViewModel.save()
                        navController.popBackStack(AppScreen.Home.name, false)
                    },
                    onCancel = { // 編集をキャンセルした場合は，元の詳細画面に戻る
                        navController.popBackStack(AppScreen.DetailRepellent.name, false)
                    },
                    onDelete = { // 削除した場合はホーム画面に戻る
                        repellentEditViewModel.delete()
                        navController.popBackStack(AppScreen.Home.name, false)
                    }
                )
            }

            // 虫除けの新規追加画面
            composable(route = AppScreen.AddRepellent.name) {
                repellentEditViewModel =
                    viewModel(factory = appViewModel.getFactoryOfRepellentAddViewModel())

                // 虫除け新規追加後はホーム画面に戻る
                val closeScreen = { navController.popBackStack(AppScreen.Home.name, false) }
                RepellentEditScreen(
                    repellentEditViewModel = repellentEditViewModel,
                    isLandscape = true,
                    onSaved = {
                        repellentEditViewModel.save()
                        closeScreen()
                    },
                    onCancel = { closeScreen() },
                    onDelete = { closeScreen() }
                )
            }
        }
    }
}
