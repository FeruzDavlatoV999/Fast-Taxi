package uz.mobile.joybox.presentation.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getNavigationBarHeight(): Int {
    return getSystemNavigationBarHeight(LocalContext.current)
}

fun getSystemNavigationBarHeight(context: Context): Int {
    return try {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId).pxToDp(context)
        } else 0
    } catch (e: Exception) {
        return 0
    }
}

fun Int.pxToDp(context: Context): Int = (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()