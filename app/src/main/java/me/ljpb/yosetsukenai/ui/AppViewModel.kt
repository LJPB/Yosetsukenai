package me.ljpb.yosetsukenai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

class AppViewModel : ViewModel() {
    private val mViewModelProvider = me.ljpb.yosetsukenai.ui.ViewModelProvider

    // 詳細画面で表示する虫除け
    private lateinit var selectedDetailRepellent: RepellentScheduleEntity

    // 編集画面で編集(表示)する虫除け
    private lateinit var selectedEditRepellent: RepellentScheduleEntity

    // 編集画面で編集(表示)する通知リスト
    private lateinit var selectedEditNotifications: List<NotificationEntity>

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
    
}