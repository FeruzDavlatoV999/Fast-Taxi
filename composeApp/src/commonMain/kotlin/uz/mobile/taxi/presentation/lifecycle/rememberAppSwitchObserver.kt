package uz.mobile.taxi.presentation.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
fun rememberAppSwitchObserver(
    onAppDidEnterBackground: () -> Unit,
    onAppDidBecomeActive: () -> Unit
): PlatformLifecycleObserver {
    val observer = remember { PlatformLifecycleObserver() }

    DisposableEffect(Unit) {
        observer.startObserving(object : AppSwitchCallback {
            override fun onAppDidEnterBackground() {
                onAppDidEnterBackground()
            }

            override fun onAppDidBecomeActive() {
                onAppDidBecomeActive()
            }
        })

        onDispose {
            observer.stopObserving()
        }
    }

    return observer
}