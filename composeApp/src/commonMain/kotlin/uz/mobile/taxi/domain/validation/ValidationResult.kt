package uz.mobile.taxi.domain.validation

import uz.mobile.taxi.domain.util.UniversalText

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: UniversalText) : ValidationResult()
}