package uz.mobile.joybox.presentation.screens.profile.settings.changePassword.forgotPasswordChange

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.repository.AuthRepository
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.domain.util.UniversalText
import uz.mobile.joybox.domain.validation.ValidatePassword
import uz.mobile.joybox.domain.validation.ValidationException
import uz.mobile.joybox.domain.validation.ValidationResult

class ProfileForgotPasswordChangePasswordViewModel(
    private val validatePassword: ValidatePassword,
    private val authRepository: AuthRepository
) : ScreenModel {


    var state: ProfileForgotPasswordChangePasswordState by mutableStateOf(ProfileForgotPasswordChangePasswordState.Idle)
        private set

    var password: String by mutableStateOf("")
        private set

    var errorPassword: UniversalText by mutableStateOf(UniversalText.Empty)
        private set


    fun updatePhone(newPhone: String) {
        password = newPhone
    }

    private fun safelyValidate(execution: () -> Unit) {
        try {
            execution()
        } catch (e: ValidationException) {
            e.printStackTrace()
        }
    }

    private fun onValidatePassword() {
        val result = validatePassword(password)

        if (result is ValidationResult.Error) {
            errorPassword = result.message
            throw ValidationException("password = $password")
        }
    }

    fun onValidate(user: User) {
        safelyValidate {
            onValidatePassword()
            sendSms(user)
        }
    }


    private fun sendSms(user: User) = screenModelScope.launch(Dispatchers.IO) {
        authRepository.passwordSms(user.phone.orEmpty()).collect { result ->
            result.onSuccess {
                state = ProfileForgotPasswordChangePasswordState.Success(password)
            }.onMessage {
                errorPassword = it
                state = ProfileForgotPasswordChangePasswordState.Idle
            }.onLoading {
                state = ProfileForgotPasswordChangePasswordState.Loading
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    errorPassword = UniversalText.Dynamic(it)
                }
                state = ProfileForgotPasswordChangePasswordState.Idle
            }
        }
    }


    fun changeState() {
        state = ProfileForgotPasswordChangePasswordState.Idle
    }
}

sealed interface ProfileForgotPasswordChangePasswordState {

    data object Loading : ProfileForgotPasswordChangePasswordState

    data object Idle : ProfileForgotPasswordChangePasswordState

    data class Success(val password: String) : ProfileForgotPasswordChangePasswordState

    data class Error(val error: String) : ProfileForgotPasswordChangePasswordState
}