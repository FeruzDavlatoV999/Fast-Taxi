package uz.mobile.taxi.presentation.screens.auth.forgotPasswordPhone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.domain.validation.ValidatePhone
import uz.mobile.taxi.domain.validation.ValidationException
import uz.mobile.taxi.domain.validation.ValidationResult

class ForgotPasswordPhoneViewModel(
    private val validatePhone: ValidatePhone,
) : ScreenModel {


    var state: PhoneForgotPasswordPhoneState by mutableStateOf(PhoneForgotPasswordPhoneState.Idle)
        private set

    var phone: String by mutableStateOf("")
        private set

    var errorPhone: UniversalText by mutableStateOf(UniversalText.Empty)
        private set


    fun updatePhone(newPhone: String) {
        phone = newPhone
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

        if (result is ValidationResult.Error) {
            errorPhone = result.message
            throw ValidationException("phone = $phone")
        }
    }

    fun onValidate() {
        safelyValidate {
            onValidatePhone()
            state = PhoneForgotPasswordPhoneState.Success("998$phone")
        }
    }


    fun changeState() {
        state = PhoneForgotPasswordPhoneState.Idle
    }
}

sealed interface PhoneForgotPasswordPhoneState {

    data object Loading : PhoneForgotPasswordPhoneState

    data object Idle : PhoneForgotPasswordPhoneState

    data class Success(val phone: String) : PhoneForgotPasswordPhoneState

    data class Error(val error: String) : PhoneForgotPasswordPhoneState
}