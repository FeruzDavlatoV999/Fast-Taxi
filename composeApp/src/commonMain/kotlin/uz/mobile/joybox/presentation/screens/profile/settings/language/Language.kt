package uz.mobile.joybox.presentation.screens.profile.settings.language

sealed class Language(val isoFormat : String) {
    data object English : Language("en")
    data object Uzbek : Language("uz")
}