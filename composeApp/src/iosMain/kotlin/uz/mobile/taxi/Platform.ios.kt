package uz.mobile.taxi

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
import platform.Foundation.NSLog
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import uz.mobile.taxi.datastore.AppLauncher
import uz.mobile.taxi.datastore.CachingManager

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun platformModule() = module {
    single { Darwin.create() }
}


actual fun cachingModule() = module {
    single { CachingManager() }
    single { AppLauncher() }
}

actual fun getScreenOrientation(): Orientation {
    val orientation = UIDevice.currentDevice.orientation
    return when (orientation) {
        UIDeviceOrientation.UIDeviceOrientationLandscapeLeft,
        UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> Orientation.LANDSCAPE

        UIDeviceOrientation.UIDeviceOrientationPortrait,
        UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> Orientation.PORTRAIT

        else -> Orientation.UNDEFINED
    }
}

actual fun getSystem(): System = System.IOS


actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(NotificationPlatformConfiguration.Ios())

}

actual fun logMessage(message: String) {
    NSLog(message)
}