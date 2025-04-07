package uz.mobile.joybox.presentation.screens.profile.settings.userUpdate

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
import uz.mobile.joybox.data.remote.dto.UpdateProfileResponse
import uz.mobile.joybox.data.repository.UserRepository

class UserUpdateViewModel(
    private val userRepository: UserRepository,
) : ScreenModel {

    var gender: String by mutableStateOf("")
        private set

    var dateOfBirth: String by mutableStateOf("")
        private set

    var userName: String by mutableStateOf("")
        private set

    var phoneNumber: String by mutableStateOf("")
        private set

    var userImage: String by mutableStateOf("")
        private set

    fun updateGender(newGender: String) {
        gender = newGender
    }

    fun updateDateOfBirth(newDateOfBirth: String) {
        dateOfBirth = newDateOfBirth
    }

    fun updateUserName(newUserName: String) {
        userName = newUserName
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        phoneNumber = newPhoneNumber
    }

    fun updateUserImage(newImage: String) {
        userImage = newImage
    }

    private val viewModelScope = screenModelScope


    private val _updateUser = MutableStateFlow<UserState>(UserState.Loading)
    val updateUser = _updateUser.asStateFlow()


    fun updateUser(
        firstName: String,
        gender: String,
        birthday: String,
        avatarFile: ByteArray?,
        avatarMimeType: String?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateProfile(
            firstName,
            gender,
            birthday,
            avatarFile,
            avatarMimeType
        ).collect { result ->
            result.onSuccess { user ->
                user?.let { user ->
                    _updateUser.emit(UserState.Success(user))
                }
            }
        }
    }

    fun getUser() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.getUser().collect { result ->
            result.onSuccess { user ->
                user?.let { item ->
                    println("phoneNumber test viewmodel ${item.phone}")
                    updateGender(item.gender ?: "")
                    updateDateOfBirth(item.birthday ?: "")
                    updateUserName(item.firstname ?: "")
                    updatePhoneNumber(item.phone ?: "")
                    updateUserImage(item.avatar ?: "")
                }
            }
        }
    }

}

sealed interface UserState {

    data object Loading : UserState

    data object Idle : UserState

    data class Success(val user: UpdateProfileResponse) : UserState

    data class Error(val error: String) : UserState
}