package uz.mobile.taxi.presentation.util

import platform.UIKit.UIApplication

actual fun setFullScreen(fullScreen: Boolean) {
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.setNeedsStatusBarAppearanceUpdate()
}