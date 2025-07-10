package uz.mobile.taxi.presentation.screens.auth.registerPassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.domain.validation.ValidatePassword
import uz.mobile.taxi.domain.validation.ValidationException
import uz.mobile.taxi.domain.validation.ValidationResult

class RegisterPasswordViewModel(
    private val validatePassword: ValidatePassword
) : ScreenModel {


    var state: PasswordState by mutableStateOf(PasswordState.Idle)
        private set

    var password: String by mutableStateOf("")
        private set

    var errorPassword: UniversalText by mutableStateOf(UniversalText.Empty)
        private set


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


    private fun onValidatePassword() {
        val result = validatePassword(password)
        if (result is ValidationResult.Error) {
            errorPassword = result.message
            throw ValidationException()
        }
    }


    fun checkPassword() = screenModelScope.launch {
        safelyValidate {
            onValidatePassword()
            state = PasswordState.Success(password)
        }
    }


    fun changeState() {
        state = PasswordState.Idle
    }
}

sealed interface PasswordState {

    data object Loading : PasswordState

    data object Idle : PasswordState

    data class Success(val password: String) : PasswordState

    data class Error(val error: String) : PasswordState
}