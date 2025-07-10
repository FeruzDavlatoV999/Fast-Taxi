package uz.mobile.taxi.presentation.components

import android.widget.Toast
import uz.mobile.taxi.App

actual fun showToastMsg(msg: String, duration: ToastDurationType) {
    App.instance?.let { context ->
        val durationType = when (duration) {
            ToastDurationType.SHORT -> Toast.LENGTH_SHORT
            ToastDurationType.LONG -> Toast.LENGTH_LONG
        }

        Toast.makeText(context, msg, durationType).show()
    }
}