package uz.mobile.joybox.domain.validation


import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.incorrect_phone_number
import joybox.composeapp.generated.resources.required_field
import uz.mobile.joybox.domain.util.UniversalText


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