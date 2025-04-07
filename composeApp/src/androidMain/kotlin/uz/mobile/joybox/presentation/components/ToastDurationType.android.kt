package uz.mobile.joybox.presentation.components

import android.widget.Toast
import uz.mobile.joybox.App

actual fun showToastMsg(msg: String, duration: ToastDurationType) {
    App.instance?.let { context ->
        val durationType = when (duration) {
            ToastDurationType.SHORT -> Toast.LENGTH_SHORT
            ToastDurationType.LONG -> Toast.LENGTH_LONG
        }

        Toast.makeText(context, msg, durationType).show()
    }
}