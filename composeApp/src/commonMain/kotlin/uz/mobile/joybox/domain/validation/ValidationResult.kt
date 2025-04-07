package uz.mobile.joybox.domain.validation

import uz.mobile.joybox.domain.util.UniversalText

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: UniversalText) : ValidationResult()
}