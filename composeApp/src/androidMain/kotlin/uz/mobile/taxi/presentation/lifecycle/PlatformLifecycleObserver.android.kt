package uz.mobile.taxi.presentation.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import uz.mobile.taxi.MainActivity

actual class PlatformLifecycleObserver {
    private var lifecycleObserver: DefaultLifecycleObserver? = null
    private var lifecycle: Lifecycle? = null

    actual fun startObserving(callback: AppSwitchCallback) {
        lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                callback.onAppDidBecomeActive()
            }


            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                callback.onAppDidEnterBackground()
            }
        }

        val activity = MainActivity.instance
        activity?.lifecycle?.addObserver(lifecycleObserver!!)
        lifecycle = activity?.lifecycle
    }

    actual fun stopObserving() {
        lifecycleObserver?.let { observer ->
            lifecycle?.removeObserver(observer)
        }
        lifecycleObserver = null
        lifecycle = null
    }
}
