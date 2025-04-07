package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.mobile.joybox.presentation.theme.getTypography


@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = getTypography().labelLarge
        )
    }
}