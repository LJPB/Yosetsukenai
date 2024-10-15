package me.ljpb.yosetsukenai.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ljpb.yosetsukenai.data.InsectAction
import me.ljpb.yosetsukenai.data.room.InsectEntity
import java.time.LocalDate
import java.time.ZoneId

class InsectEditViewModel(
    private val insect: InsectEntity?,
    private val insectAction: InsectAction
) : ViewModel() {

    // InsectEntityの新規追加か更新かのフラグ
    val isUpdate = insect != null
    
    // 発見した虫の名前
    private val _name = MutableStateFlow(insect?.name ?: "")
    val name: StateFlow<String> = _name.asStateFlow()

    // 虫を発見した日付
    private val _date = MutableStateFlow(insect?.date ?: LocalDate.now())
    val date: StateFlow<LocalDate> = _date.asStateFlow()

    // 発見した虫の大きさ
    private val _size = MutableStateFlow(insect?.size ?: "")
    val size: StateFlow<String> = _size.asStateFlow()

    // 発見した虫の状態
    private val _insectCondition = MutableStateFlow(insect?.condition ?: "")
    val insectCondition: StateFlow<String> = _insectCondition.asStateFlow()

    // 虫を発見した場所
    private val _place = MutableStateFlow(insect?.place ?: "")
    val place: StateFlow<String> = _place.asStateFlow()

    private val _zoneId = MutableStateFlow(insect?.zoneId ?: ZoneId.systemDefault())
    val zoneId: StateFlow<ZoneId> = _zoneId.asStateFlow()

    /**
     * 何らかの入力や編集があったかどうか
     */
    fun isChanged(): Boolean {
        // 編集の場合
        // 各 @@@.value の初期値は insect.@@@ だから、insect.@@@ と比較して異なるものがあれば編集済みとみなす
        // 一度編集後元の値を入力した場合は、未編集とみなされる
        if (insect != null) {
            return insect.name != name.value ||
                    insect.date != date.value ||
                    insect.size != size.value ||
                    insect.place != place.value ||
                    insect.condition != insectCondition.value
        }
        // 新規追加の場合
        // 何らかの入力があった場合(初期値は全て空文字列だから、いずれかが空文字列でない場合)は入力があったとみなす
        // 入力後に入力を削除した結果、空文字列となった場合は未入力とみなされる
        return name.value.isNotEmpty() ||
                size.value.isNotEmpty() ||
                place.value.isNotEmpty() ||
                name.value.isNotEmpty()
    }

    fun setName(name: String) {
        _name.update { name }
    }

    fun setDate(date: LocalDate) {
        _date.update { date }
    }

    fun setSize(size: String) {
        _size.update { size }
    }

    fun setInsectCondition(condition: String) {
        _insectCondition.update { condition }
    }

    fun setPlace(place: String) {
        _place.update { place }
    }

    fun save() = viewModelScope.launch {
        val newInsectEntity = InsectEntity(
            name = name.value,
            date = date.value,
            size = size.value,
            place = place.value,
            condition = insectCondition.value,
            zoneId = zoneId.value
        )
        if (insect != null) {
            insectAction.update(newInsectEntity.copy(id = insect.id))
        } else {
            insectAction.insert(newInsectEntity)
        }
    }

    fun delete() = viewModelScope.launch {
        if (insect != null) insectAction.delete(insect)
    }
}
