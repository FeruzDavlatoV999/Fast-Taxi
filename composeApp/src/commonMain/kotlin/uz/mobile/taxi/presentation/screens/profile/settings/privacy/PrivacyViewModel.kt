package uz.mobile.taxi.presentation.screens.profile.settings.privacy

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.dto.GeneralSettings
import uz.mobile.taxi.data.repository.UserRepository

class PrivacyViewModel(
    private val userRepository: UserRepository,
) : ScreenModel {

    private val viewModelScope = screenModelScope

    private val _getSettings = MutableStateFlow<PrivacyState>(PrivacyState.Loading)
    val getSettings = _getSettings.asStateFlow()

    fun onLaunch() {
        getSettings()
    }

    private fun getSettings() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.getSettings ().collect { result ->
            result.onSuccess { settings ->
                settings?.let { items ->
                    _getSettings.emit(PrivacyState.Success(items))
                }
            }.onLoading {
                _getSettings.emit(PrivacyState.Loading)
            }
        }
    }


}

sealed interface PrivacyState {

    data object Loading : PrivacyState

    data object Idle : PrivacyState

    data class Success(val settings: GeneralSettings) : PrivacyState

    data class Error(val error: String) : PrivacyState
}