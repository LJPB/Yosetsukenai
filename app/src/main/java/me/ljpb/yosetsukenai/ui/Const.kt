package me.ljpb.yosetsukenai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EscalatorWarning
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Science

object ConstIcon {
    val PRODUCT_NAME = Icons.Outlined.Science
    val START_DATE = Icons.Outlined.CalendarMonth
    val END_DATE = Icons.Outlined.CalendarMonth
    val VALIDITY_PERIOD = Icons.Outlined.Schedule
    val INSECT = Icons.Outlined.BugReport
    val PLACE = Icons.Outlined.PinDrop
    val NOTIFICATION = Icons.Outlined.Notifications
    
    val INSECT_NAME = INSECT
    val INSECT_DATE = START_DATE
    val INSECT_SIZE = Icons.Default.EscalatorWarning
    val INSECT_CONDITION = Icons.Outlined.HeartBroken
    val INSECT_PLACE = PLACE
    
    val BACK = Icons.AutoMirrored.Default.ArrowBack
    val CLOSE = Icons.Default.Close
    val CANCEL = Icons.Default.Close
    
    val FAB_ADD_REPELLENT = Icons.Default.Add
    val FAB_ADD_INSECT = Icons.Outlined.BugReport
}
