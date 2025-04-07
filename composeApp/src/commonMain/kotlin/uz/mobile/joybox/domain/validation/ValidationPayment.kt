package uz.mobile.joybox.domain.validation
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.min_payment_amount_1000
import joybox.composeapp.generated.resources.required_field
import uz.mobile.joybox.domain.util.UniversalText


class ValidationPayment : Validation {

    companion object{
        const val MIN_PAYMENT = 1000F
    }

    override fun invoke(balance: String): ValidationResult {
        return when {
            isBlank(balance) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.required_field))
            lessThenMin(balance) -> ValidationResult.Error(UniversalText.Resource(resource = Res.string.min_payment_amount_1000))
            else -> ValidationResult.Success
        }
    }


    private fun isBlank(phone: String): Boolean {
        return phone.isBlank()
    }

    private fun lessThenMin(balance: String): Boolean {
        val amount = balance.toFloatOrNull() ?: 0F
        return amount < MIN_PAYMENT
    }

}