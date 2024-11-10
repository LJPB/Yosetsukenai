package me.ljpb.yosetsukenai.ui.components.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.room.InsectEntity

@Composable
fun InsectList(
    modifier: Modifier = Modifier,
    listTitleTextStyle: TextStyle,
    textStyle: TextStyle,
    list: List<InsectEntity>,
    onClick: (InsectEntity) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = stringResource(R.string.bottom_sheet_insect_list_title),
            style = listTitleTextStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
        list.forEach {
            ListItem(
                modifier = Modifier.padding(vertical = 4.dp),
                textStyle = textStyle,
                itemName = it.name,
                onClick = { onClick(it) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InsectListPreview() {

}
