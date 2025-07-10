package uz.mobile.taxi

import androidx.compose.ui.window.ComposeUIViewController
import uz.mobile.taxi.presentation.App

fun MainViewController(toggleSplashScreen: (Boolean) -> Unit) = ComposeUIViewController {
    App(toggleSplashScreen)
}