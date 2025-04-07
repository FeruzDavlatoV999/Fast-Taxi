package uz.mobile.joybox

import android.app.Application
import org.koin.android.ext.koin.androidContext
import uz.mobile.joybox.di.initKoin

class App : Application() {
    companion object {
        var instance: App? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        initKoin(baseUrl = "http://134.119.179.100:1040/", enableNetworkLogs = false) {
            androidContext(this@App)
        }
    }
    
}