package me.ljpb.yosetsukenai.ui.screens.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.InsectAction
import me.ljpb.yosetsukenai.data.room.InsectEntity
import java.time.LocalDate

@Composable
fun InsectEditScreen(
    modifier: Modifier = Modifier,
    insectEditViewModel: InsectEditViewModel,
    isLandscape: Boolean,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    InsectEditContent(
        modifier = modifier,
        insectEditViewModel = insectEditViewModel,
        isLandscape = isLandscape,
        onSaved = onSaved,
        onCancel = onCancel,
        onDelete = onDelete
    )
}

@Preview(showBackground = true)
@Composable
fun InsectEditScreenPreview() {
    val viewModel = InsectEditViewModel(
        insect = null,
        insectAction = object : InsectAction {
            override suspend fun insert(entity: InsectEntity): Long {
                TODO("Not yet implemented")
            }

            override suspend fun update(entity: InsectEntity) {
                TODO("Not yet implemented")
            }

            override suspend fun delete(entity: InsectEntity) {
                TODO("Not yet implemented")
            }

            override fun getSize(): Long {
                TODO("Not yet implemented")
            }

            override fun getInsects(from: LocalDate, to: LocalDate): Flow<List<InsectEntity>> {
                TODO("Not yet implemented")
            }

            override fun getPagedInsects(limit: Int, offset: Int): Flow<List<InsectEntity>> {
                TODO("Not yet implemented")
            }

            override fun getMaxDate(): Flow<LocalDate?> {
                TODO("Not yet implemented")
            }

            override fun getMinDate(): Flow<LocalDate?> {
                TODO("Not yet implemented")
            }

            override fun countByDate(date: LocalDate): Flow<Int> {
                TODO("Not yet implemented")
            }
        }
    )
    InsectEditScreen(
        modifier = Modifier,
        insectEditViewModel = viewModel,
        isLandscape = true,
        onSaved = {},
        onCancel = {},
        onDelete = {}
    )
}
