package me.ljpb.yosetsukenai.ui.components.home

import androidx.compose.runtime.Composable

data class TabContent(val title: String, val content: @Composable () -> Unit)
