package me.ljpb.yosetsukenai.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.components.detail.DetailContainer
import me.ljpb.yosetsukenai.ui.components.detail.RepellentDetailContent

@Composable
fun RepellentDetailScreen(
    modifier: Modifier = Modifier,
    repellentDetailViewModel: RepellentDetailViewModel,
    insectOnClick: (InsectEntity) -> Unit,
    backButtonOnClick: () -> Unit,
    editButtonOnClick: (RepellentScheduleEntity, List<NotificationEntity>) -> Unit,
) {
    val insects by repellentDetailViewModel.insects.collectAsState()
    val notifications by repellentDetailViewModel.notifications.collectAsState()
    val repellent = repellentDetailViewModel.repellent
    DetailContainer(
        modifier = modifier,
        backButtonOnClick = backButtonOnClick,
        editButtonOnClick = { editButtonOnClick(repellent, notifications) }
    ) {
        RepellentDetailContent(
            repellent = repellent,
            insects = insects,
            notifications = notifications,
            insectOnClick = insectOnClick
        )
    }
}
