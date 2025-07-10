package uz.mobile.taxi.presentation.screens.profile.billing.balanceReplanishment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.dto.PayBalanceResponse
import uz.mobile.taxi.data.remote.dto.PromoCodeResponse
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.datastore.AppLauncher
import uz.mobile.taxi.domain.model.PaymentTypes
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.domain.validation.ValidationPayment
import uz.mobile.taxi.domain.validation.ValidationResult
import uz.mobile.taxi.presentation.screens.auth.login.LoginScreenViewModel.ValidationException

class BalanceReplenishmentViewModel(
    private val movieRepository: MovieRepository,
    private val validationPayment: ValidationPayment,
    private val appLauncher: AppLauncher,
) : ScreenModel {

    private val _getPaymentTypes =
        MutableStateFlow<BalanceReplenishmentState>(BalanceReplenishmentState.Idle)
    val getPaymentTypes = _getPaymentTypes.asStateFlow()

    private val _payBalance =
        MutableStateFlow<BalanceReplenishmentState>(BalanceReplenishmentState.Idle)
    val payBalance = _payBalance.asStateFlow()

    private val _getPromoCode =
        MutableStateFlow<BalanceReplenishmentState>(BalanceReplenishmentState.Idle)
    val getPromoCode = _getPromoCode.asStateFlow()

    var check: Boolean by mutableStateOf(false)
        private set

    var payment: Boolean by mutableStateOf(false)
        private set

    var showErrorMessage: UniversalText by mutableStateOf(UniversalText.Empty)
        private set

    var showErrorBalanceMessage: UniversalText by mutableStateOf(UniversalText.Empty)
        private set

    var paymentAlias: String by mutableStateOf("")
        private set

    var amount: String by mutableStateOf("")
        private set

    var checkPromoCode: Boolean by mutableStateOf(false)
        private set

    var promoCode: String by mutableStateOf("")
        private set

    val MIN_AMOUNT = 1_000L
    val MAX_AMOUNT = 1_000_000_000L
    val MIN_DIGITS = MIN_AMOUNT.toString().length

    init {
        getPaymentTypes("balance")
    }


    fun getUrl(url: String) {
        screenModelScope.launch(Dispatchers.IO) {
            appLauncher.getUrl(url)
        }
    }

    fun checkedPayment(newName: Boolean) {
        check = newName
    }

    fun updatePayment(newName: String) {
        paymentAlias = newName
    }

    fun updatePromoCode(check: Boolean) {
        checkPromoCode = check
    }

    fun updatePaymentBool(check: Boolean) {
        payment = check
    }

    private fun safelyValidate(execution: () -> Unit) {
        try {
            execution()
        } catch (e: ValidationException) {
            e.printStackTrace()
        }
    }

    private fun validateAmount() {
        val result = validationPayment(amount)

        when (result) {
            is ValidationResult.Error -> {
                showErrorBalanceMessage = result.message
                throw ValidationException("amount = $amount")
            }

            is ValidationResult.Success -> {
                showErrorBalanceMessage = UniversalText.Empty
            }
        }
    }


    fun onValueChanged(newValue: String) {
        safelyValidate {
            updateBalance(newValue)
            validateAmount()
        }
    }

    fun updateBalance(newName: String) {
        amount = newName
    }

    fun updatePromoCode(newName: String) {
        promoCode = newName
        showErrorMessage = UniversalText.Empty
    }


    fun getPaymentTypes(type: String) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getPaymentTypes(type).collect { result ->
            result.onSuccess { settings ->
                settings.let { items ->
                    _getPaymentTypes.emit(BalanceReplenishmentState.Success(items))
                }
            }.onLoading {
                _getPaymentTypes.emit(BalanceReplenishmentState.Loading)
            }
        }
    }


    fun onPayClicked() {
        safelyValidate {
            validateAmount()
            payBalance(amount, paymentAlias)
        }
    }


    fun payBalance(amount: String, paymentAlias: String) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.payBalance(amount, paymentAlias).collect { result ->
            result.onSuccess { promoCode ->
                promoCode.let { items ->
                    payment = true
                    _payBalance.emit(BalanceReplenishmentState.SuccessPayBalance(items))
                }
            }.onLoading {
                _payBalance.emit(BalanceReplenishmentState.Loading)
            }.onMessage {
                showErrorBalanceMessage = it
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    showErrorBalanceMessage = UniversalText.Dynamic(it)
                }
            }
        }
    }

    fun getPromoCode(code: String) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.promoCode(code).collect { result ->
            result.onSuccess { promoCode ->
                promoCode.let { items ->
                    _getPromoCode.emit(BalanceReplenishmentState.SuccessPromoCode(items))
                    checkPromoCode = true
                }
            }.onLoading {
                _getPromoCode.emit(BalanceReplenishmentState.Loading)
            }.onMessage {
                showErrorMessage = it
                _getPromoCode.emit(BalanceReplenishmentState.Idle)
            }.onErrorMap { errors ->
                errors.values.firstOrNull()?.firstOrNull()?.let {
                    showErrorMessage = UniversalText.Dynamic(it)
                    _getPromoCode.emit(BalanceReplenishmentState.Idle)
                }
            }
        }
    }

    sealed interface BalanceReplenishmentState {
        data object Loading : BalanceReplenishmentState
        data object Idle : BalanceReplenishmentState
        data class Success(val payment: List<PaymentTypes>?) : BalanceReplenishmentState
        data class SuccessPromoCode(val promoCode: PromoCodeResponse?) : BalanceReplenishmentState
        data class SuccessPayBalance(val payBalance: PayBalanceResponse?) :
            BalanceReplenishmentState

        data class Error(val error: String) : BalanceReplenishmentState
    }

}