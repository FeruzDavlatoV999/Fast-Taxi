package uz.mobile.joybox

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.LifecycleObserver
import chaintech.network.connectivitymonitor.ConnectivityMonitor
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.permission.permissionUtil
import uz.mobile.joybox.presentation.App

class MainActivity : ComponentActivity(), LifecycleObserver {

    companion object {
        var instance: MainActivity? = null
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instance = this

        var isChecking = true
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isChecking
            }
        }

        ConnectivityMonitor.initialize(this)
        enableEdgeToEdge()
        NotifierManager.onCreateOrOnNewIntent(intent)
        setContent {
            App { checking ->
                isChecking = checking
            }
        }

        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

}
