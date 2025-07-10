package uz.mobile.taxi.presentation.screens.auth.registerName

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.domain.validation.ValidateName
import uz.mobile.taxi.domain.validation.ValidationException
import uz.mobile.taxi.domain.validation.ValidationResult

class RegisterNameViewModel(
    private val validateName: ValidateName
) : ScreenModel {


    var state: NameState by mutableStateOf(NameState.Idle)
        private set


    var name: String by mutableStateOf("")
        private set

    var errorName: UniversalText by mutableStateOf(UniversalText.Empty)
        private set

    fun updateName(newName: String) {
        name = newName
    }

    private fun safelyValidate(execution: () -> Unit) {
        try {
            execution()
        } catch (e: ValidationException) {
            e.printStackTrace()
        }
    }

    private fun onValidateName() {
        val result = validateName(name)
        if (result is ValidationResult.Error) {
            errorName = result.message
            throw ValidationException("name = $name")
        }
    }

    fun onNext() {
        safelyValidate {
            onValidateName()
            state = NameState.Success(name)
        }
    }

    fun changeState() {
        state = NameState.Idle
    }


}

sealed interface NameState {

    data object Loading : NameState

    data object Idle : NameState

    data class Success(val name: String) : NameState

    data class Error(val error: UniversalText) : NameState
}