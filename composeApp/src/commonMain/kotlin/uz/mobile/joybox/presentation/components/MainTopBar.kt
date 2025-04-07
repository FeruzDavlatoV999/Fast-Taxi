package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import uz.mobile.joybox.presentation.theme.appBarTextColor
import uz.mobile.joybox.presentation.theme.getTypography

@Composable
fun MainTopBar(
    text: String,
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {


        IconButton(
            modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
            onClick = onBackButtonClicked
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = "back",
                tint = appBarTextColor
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth().weight(1f),
            textAlign = TextAlign.Center,
            text = text,
            style = getTypography().titleLarge,
            maxLines = 1,
        )


        Spacer(modifier = Modifier.width(24.dp))

    }
}