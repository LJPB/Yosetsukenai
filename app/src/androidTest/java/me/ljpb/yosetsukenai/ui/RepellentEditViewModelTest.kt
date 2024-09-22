package me.ljpb.yosetsukenai.ui

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.work.WorkManager
import me.ljpb.yosetsukenai.data.NotificationRepository
import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.RepellentScheduleRepository
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.AppDatabase
import me.ljpb.yosetsukenai.data.room.AppDatabaseConverter
import me.ljpb.yosetsukenai.data.room.TableConverter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepellentEditViewModelTest {
    private lateinit var database: AppDatabase
    private lateinit var viewModel: RepellentEditViewModel
    private lateinit var converter: AppDatabaseConverter
    private val addedNotifyListName = "addedNotifyList"
    private val deletedNotifyListName = "deletedNotifyList"
    private val toAddValue = PeriodAndTime(SimplePeriod.ofDays(1), SimpleTime.of(1, 1))
    private val toDeleteValue = PeriodAndTime(SimplePeriod.ofWeeks(3), SimpleTime.of(10, 10))

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        converter = TableConverter()

        viewModel = RepellentEditViewModel(
            null,
            listOf(),
            RepellentScheduleRepository(database.repellentScheduleDao(), converter),
            NotificationRepository(database.notificationDao(), WorkManager.getInstance(context))
        )
    }

    // 新規要素が重複なく追加されているか
    @Test
    fun addTestOne() {
        // private変数の取得
        val addedNotifyList = viewModel.javaClass.getDeclaredField(addedNotifyListName)
        addedNotifyList.isAccessible = true
        viewModel.addNotification(toAddValue)
        viewModel.addNotification(toDeleteValue)
        viewModel.addNotification(toAddValue)
        // 取得したprivate変数の値の取得
        val list = addedNotifyList.get(viewModel)
        assertEquals(list, listOf(toAddValue, toDeleteValue))
    }

    // 新規要素の削除は正しく行われているか
    @Test
    fun addTestTwo() {
        // private変数の取得
        val addedNotifyList = viewModel.javaClass.getDeclaredField(addedNotifyListName)
        addedNotifyList.isAccessible = true
        viewModel.addNotification(toAddValue)
        viewModel.addNotification(toDeleteValue)
        viewModel.removeNewNotification(toDeleteValue)
        // 取得したprivate変数の値の取得
        val list = addedNotifyList.get(viewModel)
        assertEquals(list, listOf(toAddValue))
    }

    // 追加→削除→追加でaddedNotifyListに重複して登録されないことの確認
    @Test
    fun addTestThree() {
        // private変数の取得
        val addedNotifyList = viewModel.javaClass.getDeclaredField(addedNotifyListName)
        addedNotifyList.isAccessible = true
        viewModel.addNotification(toAddValue)
        viewModel.removeNewNotification(toAddValue)
        viewModel.addNotification(toAddValue)
        // 取得したprivate変数の値の取得
        val list = addedNotifyList.get(viewModel)
        assertEquals(list, listOf(toAddValue))
    }

    // 削除した要素を保持できているか
    @Test
    fun deleteTestOne() {
        // private変数の取得
        val deletedNotifyList = viewModel.javaClass.getDeclaredField(deletedNotifyListName)
        deletedNotifyList.isAccessible = true
        viewModel.addNotification(toAddValue)
        viewModel.addNotification(toDeleteValue)
        viewModel.removeNewNotification(toDeleteValue)
        // 取得したprivate変数の値の取得
        val list = deletedNotifyList.get(viewModel)
        assertEquals(list, listOf(toDeleteValue))
    }

    // 一度削除した要素を追加後，再度削除しても重複しないか
    @Test
    fun deleteTestTwo() {
        // private変数の取得
        val deletedNotifyList = viewModel.javaClass.getDeclaredField(deletedNotifyListName)
        deletedNotifyList.isAccessible = true
        viewModel.addNotification(toDeleteValue)
        viewModel.removeNewNotification(toDeleteValue)
        viewModel.addNotification(toDeleteValue)
        viewModel.removeNewNotification(toDeleteValue)

        // 取得したprivate変数の値の取得
        val list = deletedNotifyList.get(viewModel)
        assertEquals(list, listOf(toDeleteValue))
    }

    // 繰り返し削除しても要素は重複しないかの確認
    @Test
    fun deleteTestThree() {
        // private変数の取得
        val deletedNotifyList = viewModel.javaClass.getDeclaredField(deletedNotifyListName)
        deletedNotifyList.isAccessible = true
        viewModel.addNotification(toDeleteValue)
        viewModel.removeNewNotification(toDeleteValue)
        viewModel.removeNewNotification(toDeleteValue)

        // 取得したprivate変数の値の取得
        val list = deletedNotifyList.get(viewModel)
        assertEquals(list, listOf(toDeleteValue))
    }


}