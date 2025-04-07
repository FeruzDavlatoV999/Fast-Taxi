package uz.mobile.joybox.presentation.components

enum class ToastDurationType {
    SHORT, LONG
}

expect fun showToastMsg(msg: String, duration: ToastDurationType = ToastDurationType.SHORT)