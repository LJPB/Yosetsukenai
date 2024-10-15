package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

class AppViewModel : ViewModel() {
    private val mViewModelProvider = me.ljpb.yosetsukenai.ui.ViewModelProvider

    // 虫除けの詳細画面で表示する虫除け
    private lateinit var selectedDetailRepellent: RepellentScheduleEntity

    // 発見した虫の詳細画面で表示する虫
    lateinit var selectedDetailInsect: InsectEntity
        private set

    // 編集画面で編集(表示)する虫除け
    private lateinit var selectedEditRepellent: RepellentScheduleEntity

    // 編集画面で編集(表示)する通知リスト
    private lateinit var selectedEditNotifications: List<NotificationEntity>

    // 戻るべき場所
    var backRoute = AppScreen.Home

    /**
     * 虫除けの詳細画面へ遷移するときに呼び出すメソッド
     */
    fun navigateToRepellentDetailScreen(repellent: RepellentScheduleEntity, navigate: () -> Unit) {
        selectedDetailRepellent = repellent
        navigate()
    }

    /**
     * RepellentViewModelのFactoryを取得
     */
    fun getFactoryOfRepellentDetailViewModel(): ViewModelProvider.Factory =
        mViewModelProvider.repellentDetailViewModel(selectedDetailRepellent)

    /**
     * 虫除けの編集画面へ遷移するときに呼び出すメソッド
     */
    fun navigateToRepellentEditScreen(
        repellent: RepellentScheduleEntity,
        notifications: List<NotificationEntity>,
        navigate: () -> Unit
    ) {
        selectedEditRepellent = repellent
        selectedEditNotifications = notifications
        navigate()
    }

    /**
     * RepellentEditViewModelのFactoryを取得
     */
    fun getFactoryOfRepellentEditViewModel(): ViewModelProvider.Factory =
        mViewModelProvider.repellentEditViewModel(selectedEditRepellent, selectedEditNotifications)

    /**
     * 虫除け追加画面へ遷移するときに呼び出すメソッド
     */
    fun navigateToRepellentAddScreen(navigate: () -> Unit) = navigate()

    /**
     * RepellentEditViewModelのFactoryを取得
     */
    fun getFactoryOfRepellentAddViewModel(): ViewModelProvider.Factory =
        mViewModelProvider.repellentEditViewModel(null, listOf())

    /**
     * 発見した虫の詳細画面へ遷移するときに呼び出すメソッド
     */
    fun navigateToInsectDetailScreen(insect: InsectEntity, navigate: () -> Unit) {
        selectedDetailInsect = insect
        navigate()
    }

    /**
     * 発見した虫の編集画面へ遷移するときに呼び出すメソッド
     */
    fun navigateToInsectEditScreen(insect: InsectEntity, navigate: () -> Unit) {
        selectedDetailInsect = insect
        navigate()
    }

    /**
     * 発見した虫の追加画面へ遷移するときに呼び出すメソッド
     */
    fun navigateToInsectAddScreen(insect: InsectEntity, navigate: () -> Unit) {
        selectedDetailInsect = insect
        navigate()
    }

    /**
     * InsectEditViewModelのFactoryを取得
     */
    fun getFactoryOfInsectAddViewModel(): ViewModelProvider.Factory =
        mViewModelProvider.insectEditViewModel(null)

    /**
     * InsectEditViewModelのFactoryを取得
     */
    fun getFactoryOfInsectEditViewModel(): ViewModelProvider.Factory =
        mViewModelProvider.insectEditViewModel(selectedDetailInsect)

}
