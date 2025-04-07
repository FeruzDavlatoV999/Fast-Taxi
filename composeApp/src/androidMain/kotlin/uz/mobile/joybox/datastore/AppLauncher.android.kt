package uz.mobile.joybox.datastore

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class AppLauncher(
    private val context: Context,
) {
    actual suspend fun getUrl(url: String) {
        val url = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, url)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    actual suspend fun shareProductLink(share:String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            if (shareIntent != null){
                shareIntent.putExtra(Intent.EXTRA_TEXT, share)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(shareIntent)
            }
        } catch (e: Exception) {
            println("error share $e")
        }
    }

}