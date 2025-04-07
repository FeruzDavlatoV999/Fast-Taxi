package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.not_received
import joybox.composeapp.generated.resources.resend
import org.jetbrains.compose.resources.stringResource
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.primaryLight
import uz.mobile.joybox.presentation.theme.textGrey60ColorDark
import uz.mobile.joybox.presentation.theme.timerBorderColor

@Composable
fun TimerWithResendButton(
    modifier: Modifier = Modifier,
    time: String,
    onResendClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .border(width = 1.dp, color = timerBorderColor, RoundedCornerShape(8.dp))
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = textGrey60ColorDark)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        Text(
            text = stringResource(Res.string.not_received),
            style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = textGrey60ColorDark),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(Res.string.resend),
            style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = primaryLight),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable(onClick = onResendClick)
        )
    }
}
