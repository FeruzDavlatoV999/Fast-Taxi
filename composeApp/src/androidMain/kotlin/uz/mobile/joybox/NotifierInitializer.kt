package uz.mobile.joybox

import android.content.Context
import androidx.startup.Initializer
import uz.mobile.joybox.presentation.notifications.AppInitializer

class NotifierInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AppInitializer.onApplicationStart()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}