package uz.mobile.joybox

import androidx.compose.ui.window.ComposeUIViewController
import uz.mobile.joybox.presentation.App

fun MainViewController(toggleSplashScreen: (Boolean) -> Unit) = ComposeUIViewController {
    App(toggleSplashScreen)
}