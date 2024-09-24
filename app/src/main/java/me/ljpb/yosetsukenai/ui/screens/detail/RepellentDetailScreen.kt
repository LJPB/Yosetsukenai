package me.ljpb.yosetsukenai.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.components.detail.DetailContainer
import me.ljpb.yosetsukenai.ui.components.detail.RepellentDetailContent

@Composable
fun RepellentDetailScreen(
    modifier: Modifier = Modifier,
    repellent: RepellentScheduleEntity,
    insects: List<InsectEntity>,
    notifications: List<NotificationEntity>,
    insectOnClick: (InsectEntity) -> Unit,
    backButtonOnClick: () -> Unit,
    editButtonOnClick: () -> Unit,
) {
    // TODO: 諸々の処理
    DetailContainer(
        backButtonOnClick = backButtonOnClick,
        editButtonOnClick = editButtonOnClick
    ) {
        RepellentDetailContent(
            modifier = modifier,
            repellent = repellent,
            insects = insects,
            notifications = notifications,
            insectOnClick = insectOnClick
        )
    }
}