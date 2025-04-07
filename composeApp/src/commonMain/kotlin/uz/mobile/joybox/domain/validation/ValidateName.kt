package uz.mobile.joybox.domain.validation

import uz.mobile.joybox.domain.util.UniversalText
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.required_field

class ValidateName : Validation {

    override fun invoke(name: String): ValidationResult {
        return when {
            isBlank(name) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.required_field))
            else -> ValidationResult.Success
        }
    }


    private fun isBlank(phone: String): Boolean {
        return phone.isBlank()
    }


}