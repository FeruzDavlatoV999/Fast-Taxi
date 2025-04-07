package uz.mobile.joybox.domain.util.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy


actual fun imageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray? {
    val uiImage = imageBitmap.toUIImage() ?: return null
    val nsData = UIImageJPEGRepresentation(uiImage, 1.0) ?: return null
    return nsData.toByteArray()
}

actual fun detectMimeType(imageBitmap: ImageBitmap): String {
    return "image/jpeg"
}

fun ImageBitmap.toUIImage(): UIImage? {
    return this as? UIImage
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val byteArray = ByteArray(this.length.toInt())
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), this.bytes, this.length)
    }
    return byteArray
}

actual suspend fun ImageBitmap.toByteArray(): ByteArray? {
    return Image.makeFromBitmap(asSkiaBitmap()).encodeToData()?.bytes
}