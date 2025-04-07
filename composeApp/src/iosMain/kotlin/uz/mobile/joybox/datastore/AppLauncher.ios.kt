package uz.mobile.joybox.datastore

import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class AppLauncher {
    actual suspend fun getUrl(url: String) {
        val appUrl = NSURL.URLWithString(url)

        if (appUrl != null) {
            UIApplication.sharedApplication.openURL(appUrl)
        }
    }

    actual suspend fun shareProductLink(share:String) {
        val url = NSURL.URLWithString(share) ?: ""

        val activityItems = listOf(url)
        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        dispatch_async(dispatch_get_main_queue()) {
            val rootViewController = getTopViewController()
            rootViewController?.presentViewController(
                activityViewController,
                animated = true,
                completion = null
            )
        }
    }

    private fun getTopViewController(controller: UIViewController? = null): UIViewController? {
        val rootController = controller ?: UIApplication.sharedApplication.keyWindow?.rootViewController
        return when {
            rootController == null -> null
            rootController.presentedViewController != null -> getTopViewController(rootController.presentedViewController)
            rootController is UINavigationController -> {
                rootController.visibleViewController?.let { getTopViewController(it) } ?: rootController
            }
            rootController is UITabBarController -> {
                rootController.selectedViewController?.let { getTopViewController(it) } ?: rootController
            }
            else -> rootController
        }
    }
}