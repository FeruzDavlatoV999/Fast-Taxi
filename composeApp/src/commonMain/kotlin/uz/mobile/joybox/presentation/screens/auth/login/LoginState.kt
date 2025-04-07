package uz.mobile.joybox.presentation.screens.auth.login

import uz.mobile.joybox.domain.util.UniversalText

sealed class LoginState {

    data object Loading : LoginState()

    data object Idle : LoginState()

    data object Success : LoginState()

    data class Error(val error: UniversalText) : LoginState()
}