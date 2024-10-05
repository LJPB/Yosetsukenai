package me.ljpb.yosetsukenai.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.data.room.InsectEntity
import me.ljpb.yosetsukenai.ui.components.detail.DetailContainer
import me.ljpb.yosetsukenai.ui.components.detail.InsectDetailContent
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun InsectDetailScreen(
    modifier: Modifier = Modifier,
    insect: InsectEntity,
    backButtonOnClick: () -> Unit,
    editButtonOnClick: (InsectEntity) -> Unit
) {
    DetailContainer(
        modifier = modifier,
        backButtonOnClick = backButtonOnClick,
        editButtonOnClick = { editButtonOnClick(insect) }
    ) {
        InsectDetailContent(insect = insect)
    }
}

@Preview(showBackground = true)
@Composable
fun InsectDetailScreenPreview() {
    InsectDetailScreen(
        modifier = Modifier,
        insect = InsectEntity(
            name = "insect",
            date = LocalDate.now(),
            size = "big",
            condition = "good",
            place = "entrance",
            zoneId = ZoneId.systemDefault()
        ),
        backButtonOnClick = {},
        editButtonOnClick = {}
    )
}
