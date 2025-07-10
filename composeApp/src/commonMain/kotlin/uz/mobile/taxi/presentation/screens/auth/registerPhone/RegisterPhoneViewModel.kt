package uz.mobile.taxi.presentation.screens.auth.registerPhone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.repository.AuthRepository
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.domain.validation.ValidatePhone
import uz.mobile.taxi.domain.validation.ValidationException
import uz.mobile.taxi.domain.validation.ValidationResult

class RegisterPhoneViewModel(
    private val validatePhone: ValidatePhone,
    private val authRepository: AuthRepository
) : ScreenModel {


    var state: PhoneState by mutableStateOf(PhoneState.Idle)
        private set

    var isAgree: Boolean by mutableStateOf(true)
        private set

    var phone: String by mutableStateOf("")
        private set

    var errorPhone: UniversalText by mutableStateOf(UniversalText.Empty)
        private set


    fun updatePhone(newPhone: String) {
        phone = newPhone
    }

    fun updateAgreement(agree: Boolean) {
        isAgree = agree
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


    fun onNext() {
        safelyValidate {
            onValidatePhone()
            sendSms()
        }
    }


    private fun sendSms() = screenModelScope.launch(Dispatchers.IO) {
        authRepository.sendSms("998$phone").collect { result ->
            result.onSuccess {
                state = PhoneState.Success("998$phone")
            }.onMessage {
                errorPhone = it
                state = PhoneState.Idle
            }.onLoading {
                state = PhoneState.Loading
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    errorPhone = UniversalText.Dynamic(it)
                }
                state = PhoneState.Idle
            }
        }
    }

    fun changeState() {
        state = PhoneState.Idle
    }
}

sealed interface PhoneState {

    data object Loading : PhoneState

    data object Idle : PhoneState

    data class Success(val phone: String) : PhoneState

    data class Error(val error: String) : PhoneState
}