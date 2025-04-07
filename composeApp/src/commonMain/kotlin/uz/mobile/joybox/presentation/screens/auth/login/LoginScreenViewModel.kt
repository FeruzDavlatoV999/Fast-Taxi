package uz.mobile.joybox.presentation.screens.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.repository.AuthRepository
import uz.mobile.joybox.domain.util.UniversalText
import uz.mobile.joybox.domain.validation.ValidatePassword
import uz.mobile.joybox.domain.validation.ValidatePhone
import uz.mobile.joybox.domain.validation.ValidationResult


class LoginScreenViewModel(
    private val validatePhone: ValidatePhone,
    private val validatePassword: ValidatePassword,
    private val authRepository: AuthRepository
) : ScreenModel {

    var state: LoginState by mutableStateOf(LoginState.Idle)

    var phone: String by mutableStateOf("")
        private set

    var errorPhone: UniversalText by mutableStateOf(UniversalText.Empty)
        private set

    var password: String by mutableStateOf("")
        private set

    var errorPassword: UniversalText by mutableStateOf(UniversalText.Empty)
        private set

    fun updatePhone(newPhone: String) {
        phone = newPhone
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    private fun safelyValidate(execution: () -> Unit) {
        try {
            execution()
        } catch (e: ValidationException) {
            e.printStackTrace()
        }
    }

    private fun onValidatePhone() {
        val result = validatePhone(phone)

        when (result) {
            is ValidationResult.Error -> {
                errorPhone = result.message
                throw ValidationException("phone = $phone")
            }

            is ValidationResult.Success -> {
                errorPhone = UniversalText.Empty
            }
        }
    }

    private fun onValidatePassword() {
        when (val result = validatePassword(password)) {
            is ValidationResult.Error -> {
                errorPassword = result.message
                throw ValidationException()
            }

            is ValidationResult.Success -> {
                errorPassword = UniversalText.Empty
            }
        }
    }


    fun onLogin() {
        safelyValidate {
            onValidatePhone()
            onValidatePassword()
            login(phone, password)
        }
    }


    fun login(phone: String, password: String) = screenModelScope.launch(Dispatchers.IO) {
        authRepository.login("998$phone", password).collect { data ->
            data.onSuccess {
                state = LoginState.Success
            }.onLoading {
                state = LoginState.Loading
            }.onMessage { message ->
                state = LoginState.Error(message)
            }.onErrorMap { errors ->
                errors["password"]?.firstOrNull()?.let { password ->
                    errorPassword = UniversalText.Dynamic(password)
                }

                errors["phone"]?.firstOrNull()?.let { phone ->
                    errorPhone = UniversalText.Dynamic(phone)
                }
                state = LoginState.Idle
            }
        }
    }

    fun changeState() {
        state = LoginState.Idle
    }


    class ValidationException(message: String? = null) : RuntimeException(message)

}



