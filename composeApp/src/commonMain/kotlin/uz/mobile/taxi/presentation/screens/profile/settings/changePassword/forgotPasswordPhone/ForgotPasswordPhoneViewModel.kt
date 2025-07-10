package uz.mobile.taxi.presentation.screens.profile.settings.changePassword.forgotPasswordPhone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.domain.validation.ValidatePhone
import uz.mobile.taxi.domain.validation.ValidationException
import uz.mobile.taxi.domain.validation.ValidationResult

class ProfileForgotPasswordPhoneViewModel(
    private val validatePhone: ValidatePhone,
) : ScreenModel {


    var state: ProfilePhoneForgotPasswordPhoneState by mutableStateOf(ProfilePhoneForgotPasswordPhoneState.Idle)
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
            state = ProfilePhoneForgotPasswordPhoneState.Success("998$phone")
        }
    }


    fun changeState() {
        state = ProfilePhoneForgotPasswordPhoneState.Idle
    }
}

sealed interface ProfilePhoneForgotPasswordPhoneState {

    data object Loading : ProfilePhoneForgotPasswordPhoneState

    data object Idle : ProfilePhoneForgotPasswordPhoneState

    data class Success(val phone: String) : ProfilePhoneForgotPasswordPhoneState

    data class Error(val error: String) : ProfilePhoneForgotPasswordPhoneState
}