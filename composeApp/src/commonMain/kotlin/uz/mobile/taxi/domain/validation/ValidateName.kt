package uz.mobile.taxi.domain.validation

import uz.mobile.taxi.domain.util.UniversalText
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.required_field

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