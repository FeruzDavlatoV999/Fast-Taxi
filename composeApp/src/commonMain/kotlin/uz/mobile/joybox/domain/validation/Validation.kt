package uz.mobile.joybox.domain.validation

interface Validation {
    operator fun invoke(value: String): ValidationResult
}