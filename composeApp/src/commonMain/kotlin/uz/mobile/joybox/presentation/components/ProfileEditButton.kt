package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.mobile.joybox.presentation.theme.disableButtonColor
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark

@Composable
fun ProfileEditButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = getTypography().labelMedium.copy(color = headlineMediumTextColorDark),
    onClick: () -> Unit
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = disableButtonColor, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = textStyle,
        )
    }
}