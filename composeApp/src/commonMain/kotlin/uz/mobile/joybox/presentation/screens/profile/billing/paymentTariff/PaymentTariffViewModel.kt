package uz.mobile.joybox.presentation.screens.profile.billing.paymentTariff

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.remote.dto.BalanceResponse
import uz.mobile.joybox.data.remote.dto.SubscriptionsResponse
import uz.mobile.joybox.data.repository.MovieRepository
import uz.mobile.joybox.domain.model.PaymentTypes

class PaymentTariffViewModel(
    private val movieRepository: MovieRepository,
) : ScreenModel {

    private val _getSubscription = MutableStateFlow<PaymentTariffState>(PaymentTariffState.Idle)
    val getSubscription = _getSubscription.asStateFlow()

    private val _getBalance = MutableStateFlow<PaymentTariffState>(PaymentTariffState.Idle)
    val getBalance = _getBalance.asStateFlow()

    init {
        getBalance()
        getSubscription()
    }


    fun getBalance() = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getBalance().collect { result ->
            result.onSuccess { balance ->
                balance.let { items ->
                    _getBalance.emit(PaymentTariffState.SuccessBalance(items))
                }
            }.onLoading {
                _getBalance.emit(PaymentTariffState.Loading)
            }
        }
    }

    fun getSubscription() = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getSubscriptions().collect { result ->
            result.onSuccess { subscription ->
                subscription.let { items ->
                    _getSubscription.emit(PaymentTariffState.SuccessSubscription(items))
                }
            }.onLoading {
                _getSubscription.emit(PaymentTariffState.Loading)
            }
        }
    }

    sealed interface PaymentTariffState {
        data object Loading : PaymentTariffState
        data object Idle : PaymentTariffState
        data class Success(val payment: List<PaymentTypes>?) : PaymentTariffState
        data class SuccessBalance(val payment: BalanceResponse?) : PaymentTariffState
        data class SuccessSubscription(val mySubscription: SubscriptionsResponse?) :
            PaymentTariffState

        data class Error(val error: String) : PaymentTariffState
    }

}