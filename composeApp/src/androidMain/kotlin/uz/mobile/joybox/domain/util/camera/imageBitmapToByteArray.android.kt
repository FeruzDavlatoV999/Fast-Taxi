package uz.mobile.joybox.domain.util.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

actual fun imageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray? {
    val bitmap = imageBitmap.asAndroidBitmap()
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}

actual fun detectMimeType(imageBitmap: ImageBitmap): String {
    val bitmap = imageBitmap.asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
    return options.outMimeType ?: "image/jpeg"
}


actual suspend fun ImageBitmap.toByteArray(): ByteArray? {
    return withContext(Dispatchers.IO) {
        val bitmap = asAndroidBitmap()

        ByteArrayOutputStream().use { baos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            baos.toByteArray()
        }
    }
}