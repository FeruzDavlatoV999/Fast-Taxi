package uz.mobile.taxi.presentation.lifecycle

expect class PlatformLifecycleObserver() {
    fun startObserving(callback: AppSwitchCallback)
    fun stopObserving()
}