
package uz.mobile.taxi.presentation.screens.profile.settings.changePassword.forgotPasswordOtp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.repository.AuthRepository
import uz.mobile.taxi.domain.model.User
import uz.mobile.taxi.domain.util.UniversalText

class ProfileForgetPasswordOtpViewModel(
    private val authRepository: AuthRepository
) : ScreenModel {


    var state: ProfileForgetPasswordOtpState by mutableStateOf(ProfileForgetPasswordOtpState.Idle)
        private set

    var otp: String by mutableStateOf("")
        private set

    var time: String by mutableStateOf("01:59")
        private set

    var timer: Int by mutableStateOf(120)

    var errorOtp: UniversalText by mutableStateOf(UniversalText.Empty)
        private set

    private var timerJob: Job? = null


    fun updateOtp(newOtp: String, isLast: Boolean) {
        otp = newOtp
    }


    fun changeState() {
        state = ProfileForgetPasswordOtpState.Idle
    }

    fun onRegister(user: User) = screenModelScope.launch(Dispatchers.IO) {
        authRepository.passwordReset(user, otp).collect { result ->
            result.onSuccess {
                state = ProfileForgetPasswordOtpState.Success
            }.onMessage {
                state = ProfileForgetPasswordOtpState.Error(it.dynamic())
            }.onLoading {
                state = ProfileForgetPasswordOtpState.Loading
            }
        }
    }

    fun sendSms(phone: String) = screenModelScope.launch(Dispatchers.IO) {
        authRepository.passwordSms(phone).collect { result ->
            result.onSuccess {
                state = ProfileForgetPasswordOtpState.Success
            }.onMessage {
                state = ProfileForgetPasswordOtpState.Error(it.dynamic())
            }.onLoading {
                state = ProfileForgetPasswordOtpState.Loading
            }
        }
    }

    fun startTimer() {
        timerJob?.cancel()
        timer = 120
        timerJob = screenModelScope.launch {
            while (timer > 0) {
                delay(1000)
                timer--
                time = secondToTime(timer)
            }
        }
    }

    private fun secondToTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val minutesString = if (minutes < 10) "0$minutes" else minutes.toString()
        val secondsString = if (remainingSeconds < 10) "0$remainingSeconds" else remainingSeconds.toString()
        return "$minutesString:$secondsString"
    }
}

sealed interface ProfileForgetPasswordOtpState {

    data object Loading : ProfileForgetPasswordOtpState

    data object Idle : ProfileForgetPasswordOtpState

    data object Success : ProfileForgetPasswordOtpState

    data class Error(val error: String) : ProfileForgetPasswordOtpState
}