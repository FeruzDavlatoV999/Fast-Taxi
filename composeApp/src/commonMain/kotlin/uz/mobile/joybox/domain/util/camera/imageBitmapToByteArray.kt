package uz.mobile.joybox.domain.util.camera

import androidx.compose.ui.graphics.ImageBitmap

expect fun imageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray?
expect fun detectMimeType(imageBitmap: ImageBitmap): String

expect suspend fun ImageBitmap.toByteArray(): ByteArray?