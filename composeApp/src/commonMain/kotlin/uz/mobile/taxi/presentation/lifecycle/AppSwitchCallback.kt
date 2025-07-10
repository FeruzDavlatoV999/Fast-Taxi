package uz.mobile.taxi.presentation.lifecycle

interface AppSwitchCallback {
    fun onAppDidEnterBackground()
    fun onAppDidBecomeActive()
}