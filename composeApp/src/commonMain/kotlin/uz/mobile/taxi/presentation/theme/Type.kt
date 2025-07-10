package uz.mobile.taxi.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font

@Composable
fun InterFontFamily() = FontFamily(
    Font(Res.font.inter_light, weight = FontWeight.Light),
    Font(Res.font.inter_normal, weight = FontWeight.Normal),
    Font(Res.font.inter_medium, weight = FontWeight.Medium),
    Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.inter_bold, weight = FontWeight.Bold)
)

@Composable
fun getTypography(): Typography {


    val fontFamily = InterFontFamily()
    val baseline = Typography()

    return Typography(
        displayLarge = baseline.displayLarge.copy(
            fontFamily = fontFamily,
            color = headlineMediumTextColorDark,
        ),
        displayMedium = baseline.displayMedium.copy(
            fontFamily = fontFamily,
            color = Color.White,
            lineHeight = 18.sp,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        ),
        displaySmall = baseline.displaySmall.copy(
            fontFamily = fontFamily,
            color = darkGreyBodyTextColor,
            lineHeight = 16.sp,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        ),
        headlineLarge = baseline.headlineLarge.copy(
            fontFamily = fontFamily,
            color = headlineMediumTextColorDark,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium
        ),

        headlineMedium = baseline.headlineMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            color = headlineMediumTextColorDark,
            fontSize = 30.sp,
            lineHeight = 35.sp
        ),
        headlineSmall = baseline.headlineSmall.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            color = headlineMediumTextColorDark,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        titleLarge = baseline.titleLarge.copy(
            fontFamily = fontFamily,
            color = headlineMediumTextColorDark,
            fontSize = 18.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight.Bold
        ),
        titleMedium = baseline.titleMedium.copy(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = headlineMediumTextColorDark,
            fontWeight = FontWeight.SemiBold,
        ),
        titleSmall = baseline.titleSmall.copy(
            fontFamily = fontFamily,
            color = headlineMediumTextColorDark,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 14.sp
        ),
        bodyLarge = baseline.bodyLarge.copy(
            fontFamily = fontFamily,
            color = bodyLargeTextColorDark,
            lineHeight = 21.sp,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
        ),
        bodyMedium = baseline.bodyMedium.copy(
            fontFamily = fontFamily,
            color = bodyLargeTextColorDark,
            lineHeight = 17.sp,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
        ),
        bodySmall = baseline.bodySmall.copy(
            fontFamily = fontFamily,
            color = bodyLargeTextColorDark,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            fontSize = 14.sp
        ),
        labelLarge = baseline.labelLarge.copy(
            fontFamily = fontFamily,
            color = secondaryButtonTextColorDark,
            fontWeight = FontWeight.Medium,
            lineHeight = 21.sp,
            fontSize = 18.sp
        ),
        labelMedium = baseline.labelMedium.copy(
            fontFamily = fontFamily,
            color = mainButtonTextColorDark,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            fontSize = 16.sp,
        ),
        labelSmall = baseline.labelSmall.copy(
            fontFamily = fontFamily,
            color = darkGreyTextColorDark,
            lineHeight = 16.sp,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        ),
    )

}


