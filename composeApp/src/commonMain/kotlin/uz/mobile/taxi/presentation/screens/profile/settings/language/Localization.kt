package uz.mobile.taxi.presentation.screens.profile.settings.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalLocalization = staticCompositionLocalOf { Language.English.isoFormat }

@Composable
fun LocalizedApp(language: String = Language.Uzbek.isoFormat, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalLocalization provides language,
        content = content
    )
}

