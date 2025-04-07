package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun HeaderTitle(title: String) {
    Text(
        text = title,
        color = Color.LightGray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier.fillMaxWidth()
    )
}