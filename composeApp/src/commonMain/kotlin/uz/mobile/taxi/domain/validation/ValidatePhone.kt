package uz.mobile.taxi.domain.validation


import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.incorrect_phone_number
import taxi.composeapp.generated.resources.required_field
import uz.mobile.taxi.domain.util.UniversalText


class ValidatePhone : Validation {

    override fun invoke(phone: String): ValidationResult {
        return when {
            isBlank(phone) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.required_field))
            lessThenLength(phone) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.incorrect_phone_number))
            else -> ValidationResult.Success
        }
    }


    private fun isBlank(phone: String): Boolean {
        return phone.isBlank()
    }

    private fun lessThenLength(phone: String): Boolean {
        return phone.length != 9
    }

}