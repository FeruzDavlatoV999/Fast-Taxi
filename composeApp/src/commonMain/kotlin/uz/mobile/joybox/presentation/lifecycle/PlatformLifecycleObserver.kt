package uz.mobile.joybox.presentation.lifecycle

expect class PlatformLifecycleObserver() {
    fun startObserving(callback: AppSwitchCallback)
    fun stopObserving()
}