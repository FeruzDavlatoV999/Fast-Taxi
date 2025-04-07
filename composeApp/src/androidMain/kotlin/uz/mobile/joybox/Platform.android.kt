package uz.mobile.joybox

import android.content.res.Configuration
import android.os.Build
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import io.ktor.client.engine.android.Android
import org.koin.dsl.module
import uz.mobile.joybox.datastore.AppLauncher
import uz.mobile.joybox.datastore.CachingManager

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}


actual fun getPlatform(): Platform = AndroidPlatform()

actual fun cachingModule() = module {
    single { CachingManager(get()) }
    single { AppLauncher(get()) }
}

actual fun platformModule() = module {
    single { Android.create() }
}

actual fun getScreenOrientation(): Orientation {
    val context = App.instance
    return when (context?.resources?.configuration?.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> Orientation.LANDSCAPE
        Configuration.ORIENTATION_PORTRAIT -> Orientation.PORTRAIT
        else -> Orientation.UNDEFINED
    }
}

actual fun getSystem(): System = System.ANDROID


actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.ic_launcher_foreground,
        )
    )
}

actual fun logMessage(message: String) {

}