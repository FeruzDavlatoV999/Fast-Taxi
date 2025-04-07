package uz.mobile.joybox.presentation.lifecycle

interface AppSwitchCallback {
    fun onAppDidEnterBackground()
    fun onAppDidBecomeActive()
}