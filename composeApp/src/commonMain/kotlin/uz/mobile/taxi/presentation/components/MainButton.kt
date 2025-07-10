package uz.mobile.taxi.presentation.components

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.mobile.taxi.presentation.theme.disableButtonColor
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.primaryLight
import uz.mobile.taxi.presentation.theme.progressBarBackgroundColor


@Composable
fun MainButton(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnable: Boolean = true,
    enableButtonColor:Color = primaryLight,
    textStyle:TextStyle = getTypography().labelMedium,
    onClicked: () -> Unit,
) {

    val buttonColor = if (isEnable && !isLoading) enableButtonColor else disableButtonColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
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
                style = textStyle,
            )
        }
    }
}