package uz.mobile.taxi.presentation.screens.auth.registerOtp

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

class RegisterOtpScreenViewModel(
    private val authRepository: AuthRepository
) : ScreenModel {


    var state: OtpState by mutableStateOf(OtpState.Idle)

    var time: String by mutableStateOf("01:59")
        private set

    var timer: Int by mutableStateOf(120)


    var otp: String by mutableStateOf("")
        private set

    var errorOtp: UniversalText by mutableStateOf(UniversalText.Empty)
        private set


    fun updateOtp(newOtp: String, isLast: Boolean) {
        otp = newOtp
        errorOtp = UniversalText.Empty
    }

    fun clearOtp() {
        otp = ""
    }

    private var timerJob: Job? = null


    fun register(userData: User) = screenModelScope.launch(Dispatchers.IO) {
        if (otp.isBlank()) return@launch
        authRepository.register(userData, otp).collect { result ->
            result.onSuccess {
                state = OtpState.Success
            }.onMessage {
                state = OtpState.Error(it)
            }.onLoading {
                state = OtpState.Loading
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    errorOtp = UniversalText.Dynamic(it)
                }
                state = OtpState.Idle
            }
        }
    }

    fun resendOtp(phone: String) = screenModelScope.launch(Dispatchers.IO) {
        errorOtp = UniversalText.Empty
        authRepository.sendSms(phone).collect { result ->
            result.onSuccess {
                clearOtp()
                startTimer()
                state = OtpState.Idle
            }.onMessage {
                state = OtpState.Error(it)
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    state = OtpState.Error(UniversalText.Dynamic(it))
                }
                state = OtpState.Idle
            }.onLoading {
                state = OtpState.Loading
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

    fun changeState() {
        state = OtpState.Idle
    }


}

sealed interface OtpState {

    data object Loading : OtpState

    data object Idle : OtpState

    data object Success : OtpState

    data class Error(val error: UniversalText) : OtpState
}