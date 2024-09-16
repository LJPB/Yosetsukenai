package me.ljpb.yosetsukenai.ui.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.InsectEncounterEntity
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.common.InsectTag
import me.ljpb.yosetsukenai.ui.components.common.NotificationTag
import me.ljpb.yosetsukenai.ui.components.common.PlaceTag
import me.ljpb.yosetsukenai.ui.components.common.RowItem
import me.ljpb.yosetsukenai.ui.components.common.RowItemWithText
import me.ljpb.yosetsukenai.ui.getTextOfLocalDate
import me.ljpb.yosetsukenai.ui.getTextOfNotificationEntity
import me.ljpb.yosetsukenai.ui.getTextOfSimplePeriod
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@Composable
fun RepellentDetailContent(
    modifier: Modifier = Modifier,
    repellent: RepellentScheduleEntity,
    insects: List<InsectEncounterEntity>,
    notifications: List<NotificationEntity>,
    insectOnClick: (InsectEncounterEntity) -> Unit,
) {
    val context = LocalContext.current
    val name = repellent.name
    val startDate = repellent.startDate
    val finishDate = repellent.finishDate
    val validityPeriod = repellent.validityPeriod
    val places = repellent.places

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.detail_content_vertical_padding))
    ) {
        // 商品名
        RowItemWithText(
            leadingIcon = ConstIcon.PRODUCT_NAME,
            itemName = stringResource(id = R.string.repellent_name),
            text = name
        )
        HorizontalDivider()

        // 開始日
        RowItemWithText(
            leadingIcon = ConstIcon.START_DATE,
            itemName = stringResource(id = R.string.repellent_start_date),
            text = getTextOfLocalDate(startDate, context)
        )
        HorizontalDivider()

        // 終了日
        RowItemWithText(
            leadingIcon = ConstIcon.END_DATE,
            itemName = stringResource(id = R.string.repellent_end_date),
            text = getTextOfLocalDate(finishDate, context)
        )
        HorizontalDivider()

        // 有効期間
        RowItemWithText(
            leadingIcon = ConstIcon.VALIDITY_PERIOD,
            itemName = stringResource(id = R.string.repellent_validity_period),
            text = stringResource(
                id = R.string.validity_period_text,
                getTextOfSimplePeriod(validityPeriod, context)
            )
        )
        HorizontalDivider()

        // 発見した虫の一覧
        RowItem(
            leadingIcon = ConstIcon.INSECT,
            itemName = stringResource(id = R.string.repellent_insect)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                insects.forEach { insect ->
                    InsectTag(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.row_item_height))
                            .padding(vertical = 4.dp),
                        text = insect.name,
                        enabled = true
                    ) {
                        insectOnClick(insect)
                    }
                }
            }
        }
        HorizontalDivider()

        // 場所
        RowItem(
            leadingIcon = ConstIcon.PLACE,
            itemName = stringResource(id = R.string.repellent_place)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                places.forEach { place ->
                    PlaceTag(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.row_item_height))
                            .padding(vertical = 4.dp),
                        text = place
                    )
                }
            }
        }
        HorizontalDivider()

        // 通知
        RowItem(
            leadingIcon = ConstIcon.NOTIFICATION,
            itemName = stringResource(id = R.string.repellent_notification)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                notifications.forEach { notification ->
                    NotificationTag(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.row_item_height))
                            .padding(vertical = 4.dp),
                        text = getTextOfNotificationEntity(notification, context)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepellentDetailPreview() {
    RepellentDetailContent(
        repellent = RepellentScheduleEntity(
            name = "aaaaaaaa",
            validityPeriod = SimplePeriod.ofDays(30),
            startDate = LocalDate.of(2024, 9, 1),
            finishDate = LocalDate.of(2024, 10, 1),
//            places = listOf("aaa", "bbb", "ccc"),
            places = listOf(),
            ignore = false,
            zoneId = ZoneId.of("UTC")
        ),
        insects = listOf(
            InsectEncounterEntity(
                name = "aaa",
                date = LocalDate.of(2024, 9, 3),
                size = "大きい",
                condition = "瀕死",
                place = "玄関",
                zoneId = ZoneId.of("UTC")
            ),
            InsectEncounterEntity(
                name = "aaa",
                date = LocalDate.of(2024, 9, 3),
                size = "大きい",
                condition = "瀕死",
                place = "玄関",
                zoneId = ZoneId.of("UTC")
            ),
            InsectEncounterEntity(
                name = "aaa",
                date = LocalDate.of(2024, 9, 3),
                size = "大きい",
                condition = "瀕死",
                place = "玄関",
                zoneId = ZoneId.of("UTC")
            ),
        ),
        notifications = listOf(
            NotificationEntity(
                repellentScheduleId = 1,
                jobId = UUID.randomUUID(),
                notificationId = 1,
                triggerTimeSeconds = 1,
                schedule = SimplePeriod.ofDays(3),
                time = SimpleTime.of(1, 1)
            ),
            NotificationEntity(
                repellentScheduleId = 1,
                jobId = UUID.randomUUID(),
                notificationId = 1,
                triggerTimeSeconds = 1,
                schedule = SimplePeriod.ofDays(3),
                time = SimpleTime.of(1, 1)
            ),
            NotificationEntity(
                repellentScheduleId = 1,
                jobId = UUID.randomUUID(),
                notificationId = 1,
                triggerTimeSeconds = 1,
                schedule = SimplePeriod.ofDays(3),
                time = SimpleTime.of(1, 1)
            ),
        )
    ) {}
}