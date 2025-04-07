package uz.mobile.joybox.presentation.notifications

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import uz.mobile.joybox.onApplicationStartPlatformSpecific

object AppInitializer {
    fun onApplicationStart() {
        onApplicationStartPlatformSpecific()
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {

            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)

            }
        })
    }
}