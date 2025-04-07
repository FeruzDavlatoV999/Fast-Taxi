package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) { index ->
            IndicatorDot(isSelected = index == currentPage)
        }
    }
}


@Composable
fun IndicatorDot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = Color.White,
                shape = CircleShape
            )
    )
}