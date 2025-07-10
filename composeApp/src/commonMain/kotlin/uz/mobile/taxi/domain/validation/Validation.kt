package uz.mobile.taxi.domain.validation

interface Validation {
    operator fun invoke(value: String): ValidationResult
}