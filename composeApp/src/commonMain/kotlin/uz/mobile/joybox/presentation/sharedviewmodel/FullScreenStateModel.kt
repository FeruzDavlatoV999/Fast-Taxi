package uz.mobile.joybox.presentation.sharedviewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class FullScreenStateModel : ScreenModel {


    var isFullScreen by mutableStateOf(false)
        private set


    fun setFS(isFS:Boolean){
        isFullScreen = isFS
    }

}