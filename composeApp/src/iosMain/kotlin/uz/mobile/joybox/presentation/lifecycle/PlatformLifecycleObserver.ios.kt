package uz.mobile.joybox.presentation.lifecycle

import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.darwin.NSObjectProtocol

actual class PlatformLifecycleObserver {
    private var notificationCenter: NSNotificationCenter? = null
    private var activeObserver: NSObjectProtocol? = null
    private var backgroundObserver: NSObjectProtocol? = null

    actual fun startObserving(callback: AppSwitchCallback) {
        notificationCenter = NSNotificationCenter.defaultCenter

        activeObserver = notificationCenter?.addObserverForName(
            UIApplicationDidBecomeActiveNotification,
            null,
            null
        ) { _ -> Unit
            callback.onAppDidBecomeActive()
        }

        backgroundObserver = notificationCenter?.addObserverForName(
            UIApplicationDidEnterBackgroundNotification,
            null,
            null
        ) { _ -> Unit
            callback.onAppDidEnterBackground()
        }
    }

    actual fun stopObserving() {
        activeObserver?.let { notificationCenter?.removeObserver(it) }
        backgroundObserver?.let { notificationCenter?.removeObserver(it) }
        activeObserver = null
        backgroundObserver = null
        notificationCenter = null
    }
}