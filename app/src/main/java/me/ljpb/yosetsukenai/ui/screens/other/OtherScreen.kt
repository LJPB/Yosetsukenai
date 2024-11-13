package me.ljpb.yosetsukenai.ui.screens.other

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.ui.ConstIcon
import me.ljpb.yosetsukenai.ui.components.other.OtherContentItem
import me.ljpb.yosetsukenai.ui.components.other.OtherScreenContainer

@Composable
fun OtherScreen(
    modifier: Modifier = Modifier,
) {
    val uri = Uri.parse(stringResource(R.string.privacy_policy_url))
    val context = LocalContext.current
    OtherScreenContainer(modifier = modifier.padding(top = dimensionResource(R.dimen.detail_content_vertical_padding))) { 
        OtherContentItem(
            icon = ConstIcon.PRIVACY,
            text = stringResource(R.string.privacy_policy_text),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OtherScreenPreview() {
    OtherScreen(
        modifier = Modifier,
    )
}
