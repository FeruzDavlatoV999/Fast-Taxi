package uz.mobile.joybox.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobile.joybox.System
import uz.mobile.joybox.data.remote.dto.GeneralSettings
import uz.mobile.joybox.data.repository.UserRepository
import uz.mobile.joybox.datastore.AppLauncher
import uz.mobile.joybox.datastore.CachingManager
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.getSystem

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val appLauncher: AppLauncher,
    private val cache: CachingManager,
) : ScreenModel {
    private val viewModelScope = screenModelScope

    private val androidUrl = "https://play.google.com/store/apps/details?id=uz.makontv.mobile&hl=en_US"
    private val iosUrl = "https://apps.apple.com/sa/app/makontv/id6736584491"

    private val _getUser = MutableStateFlow<UserState>(UserState.Idle)
    val getUser = _getUser.asStateFlow()

    private val _getSettings = MutableStateFlow<UserState>(UserState.Idle)
    val getSettings = _getSettings.asStateFlow()

    fun onLaunch() {
        getUser()
        getSettings()
    }

    private fun getUser() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.getUser().collect { result ->
            result.onSuccess { user ->
                user?.let { user ->
                    _getUser.emit(UserState.Success(user))
                }
            }
        }
    }

    private fun getSettings() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.getSettings().collect { result ->
            result.onSuccess { settings ->
                settings?.let { items ->
                    _getSettings.emit(UserState.SuccessSettings(items))
                }
            }
        }
    }

    fun getUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appLauncher.getUrl(url)
        }
    }

    fun shareData(){
        viewModelScope.launch(Dispatchers.IO) {
            if (getSystem().name == System.ANDROID.name) appLauncher.shareProductLink(androidUrl)
            else appLauncher.shareProductLink(iosUrl)


        }
    }

    var isChecked by  mutableStateOf(false)

    val notification get() = cache.getNotification()

    fun saveNotification(notification: Boolean) {
        viewModelScope.launch {
            cache.saveNotification(notification)
        }
    }

}

sealed interface UserState {

    data object Loading : UserState

    data object Idle : UserState

    data class Success(val user: User) : UserState
    data class SuccessSettings(val settings: GeneralSettings) : UserState

    data class Error(val error: String) : UserState
}