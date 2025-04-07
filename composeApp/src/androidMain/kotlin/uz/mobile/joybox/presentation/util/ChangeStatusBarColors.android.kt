package uz.mobile.joybox.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
actual fun ChangeStatusBarColors(statusBarColor: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(statusBarColor)
    systemUiController.setNavigationBarColor(Color(0xFF0F1111))
}


