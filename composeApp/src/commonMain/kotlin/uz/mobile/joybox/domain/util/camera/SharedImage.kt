package uz.mobile.joybox.domain.util.camera

import androidx.compose.ui.graphics.ImageBitmap

expect class SharedImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}