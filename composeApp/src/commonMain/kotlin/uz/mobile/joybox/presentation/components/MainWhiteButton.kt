package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.theme.progressBarBackgroundColor


@Composable
fun MainWhiteButton(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnable: Boolean = true,
    onClicked: () -> Unit,
) {

    val buttonColor = Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(color = buttonColor, shape = RoundedCornerShape(8.dp))
            .clickable(enabled = !isLoading && isEnable) { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp).align(Alignment.Center),
                color = headlineMediumTextColorDark,
                backgroundColor = progressBarBackgroundColor
            )
        } else {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = getTypography().labelMedium
            )
        }
    }
}