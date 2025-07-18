package uz.mobile.taxi.domain.validation

import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.password_length_error
import taxi.composeapp.generated.resources.required_field
import uz.mobile.taxi.domain.util.UniversalText

class ValidatePassword : Validation {

    companion object {
        private const val MINIMUM_LENGTH = 6
    }

    override operator fun invoke(password: String): ValidationResult {
        return when {
            isBlank(password) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.required_field))
            lessThanMinimum(password) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.password_length_error))
            else -> ValidationResult.Success
        }
    }

    private fun lessThanMinimum(password: String): Boolean {
        return password.length < MINIMUM_LENGTH
    }

    private fun isBlank(password: String): Boolean {
        return password.isBlank()
    }


}