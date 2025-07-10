package uz.mobile.taxi.presentation.platform

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey


interface MoviesAppScreen : Screen {

    fun <T> onResult(obj: T) {}

    var backHandler: (() -> Unit)?
        get() = null
        set(newBackHandler: (() -> Unit)?) {
            backHandler = newBackHandler
        }


    var swipeEnabled: Boolean
        get() = true
        set(value) {
            swipeEnabled = value
        }

    @Composable
    override fun Content()

    override val key: ScreenKey get() = super.key



}