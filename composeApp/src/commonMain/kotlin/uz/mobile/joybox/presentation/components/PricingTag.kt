package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark

@Composable
fun PricingTag(
    id:Int,
    text: String,
    isSelected: Boolean = false,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val bgColor = if (isSelected) headlineMediumTextColorDark else Color.Transparent
    val textStyle = if (isSelected) getTypography().labelMedium.copy(lineHeight = 16.sp)
    else getTypography().bodySmall.copy(lineHeight = 16.sp, color = headlineMediumTextColorDark)

    Text(
        text = text.ifEmpty { "Season $id" },
        modifier = modifier.background(
            color = bgColor,
            shape = RoundedCornerShape(50)
        ).padding(horizontal = 20.dp, vertical = 8.dp).clickable { onSelected(id) },
        style = textStyle
    )
}