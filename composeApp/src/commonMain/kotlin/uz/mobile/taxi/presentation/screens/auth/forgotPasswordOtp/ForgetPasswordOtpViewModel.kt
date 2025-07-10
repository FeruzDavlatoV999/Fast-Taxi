package uz.mobile.taxi.presentation.screens.auth.forgotPasswordOtp

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

class ForgetPasswordOtpViewModel(
    private val authRepository: AuthRepository
) : ScreenModel {


    var state: ForgetPasswordOtpState by mutableStateOf(ForgetPasswordOtpState.Idle)
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

    fun clearOtp() {
        otp = ""
    }


    fun changeState() {
        state = ForgetPasswordOtpState.Idle
    }

    fun onRegister(user: User) = screenModelScope.launch(Dispatchers.IO) {
        authRepository.passwordReset(user, otp).collect { result ->
            result.onSuccess {
                state = ForgetPasswordOtpState.Success
            }.onMessage {
                state = ForgetPasswordOtpState.Error(it)
            }.onLoading {
                state = ForgetPasswordOtpState.Loading
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    errorOtp = UniversalText.Dynamic(it)
                }
                state = ForgetPasswordOtpState.Idle
            }
        }
    }

    fun sendSms(phone: String) = screenModelScope.launch(Dispatchers.IO) {
        authRepository.passwordSms(phone).collect { result ->
            result.onSuccess {
                clearOtp()
                startTimer()
                state = ForgetPasswordOtpState.Idle
            }.onMessage {
                state = ForgetPasswordOtpState.Error(it)
            }.onLoading {
                state = ForgetPasswordOtpState.Loading
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    state = ForgetPasswordOtpState.Error(UniversalText.Dynamic(it))
                }
                state = ForgetPasswordOtpState.Idle
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

sealed interface ForgetPasswordOtpState {

    data object Loading : ForgetPasswordOtpState

    data object Idle : ForgetPasswordOtpState

    data object Success : ForgetPasswordOtpState

    data class Error(val error: UniversalText) : ForgetPasswordOtpState
}