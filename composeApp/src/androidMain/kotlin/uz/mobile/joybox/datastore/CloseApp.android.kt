package uz.mobile.joybox.datastore

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
actual fun CloseApp(closeAction: () -> Unit) {

    val activity = LocalContext.current as? Activity
    closeAction.invoke()
    activity?.finish()
}