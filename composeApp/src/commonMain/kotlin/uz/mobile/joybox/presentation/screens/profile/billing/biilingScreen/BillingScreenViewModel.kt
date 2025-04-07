package uz.mobile.joybox.presentation.screens.profile.billing.biilingScreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.remote.dto.SubscriptionBuyResponse
import uz.mobile.joybox.data.repository.MovieRepository
import uz.mobile.joybox.datastore.AppLauncher
import uz.mobile.joybox.domain.model.BuySubscriptionRequest
import uz.mobile.joybox.domain.model.PaymentTypes
import uz.mobile.joybox.domain.util.UniversalText

class BillingScreenViewModel(
    private val movieRepository: MovieRepository,
    private val appLauncher: AppLauncher,
    private val id: Int
) : ScreenModel {

    private val _networkStatus = MutableStateFlow(ConnectivityStatus.CONNECTED)
    val networkStatus = _networkStatus.asStateFlow()

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()

    private val _subscriptionState = MutableStateFlow(SubscriptionState())
    val subscriptionState = _subscriptionState.asStateFlow()


    init {
        loadPaymentTypes()
    }


    fun loadPaymentTypes() = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getPaymentTypes("").collect { result ->
            result.onSuccess { list ->
                _paymentState.update { it.copy(payments = list.orEmpty(), isLoading = false) }
            }.onLoading {
                _paymentState.update { it.copy(isLoading = true) }
            }.onMessage { message ->
                _paymentState.update { it.copy(errorMessage = message, isLoading = false) }
            }.onErrorMap { message ->
                _paymentState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun initiateSubscription(paymentAlias: String) = screenModelScope.launch(Dispatchers.IO) {
        val request = BuySubscriptionRequest(
            productId = id,
            productType = "subscription",
            paymentAlias = paymentAlias,
            promCode = null
        )

        movieRepository.buySubscription(request).collect { result ->
            result.onSuccess { data ->
                handleSuccessfulSubscription(data)
            }.onMessage { error ->
                handleSubscriptionError(error)
            }.onLoading {
                _subscriptionState.update { it.copy(isLoading = true) }
            }
        }
    }

    private fun handleSuccessfulSubscription(response: SubscriptionBuyResponse?) {
        _subscriptionState.update {
            it.copy(
                successResponse = response,
                isLoading = false
            )
        }
    }

    private fun handleSubscriptionError(message: UniversalText) {
        _subscriptionState.update {
            it.copy(
                showError = true,
                errorMessage = message,
                isLoading = false
            )
        }
    }

    fun openPaymentUrl(url: String) {
        _subscriptionState.update { it.copy(successResponse = null) }
        screenModelScope.launch(Dispatchers.IO) {
            appLauncher.getUrl(url)
        }
    }

    fun clearSubscriptionError() {
        _subscriptionState.update {
            it.copy(
                showError = false,
                errorMessage = UniversalText.Empty
            )
        }
    }

    fun handleAppBackground() {
        _subscriptionState.update { it.copy(hasMoved = true) }
    }

    fun checkSubscriptionStatus(id: Int) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getMySubscriptions(true).collect { result ->
            result.onSuccess { subscription ->
                subscription?.subscriptions?.find { it.id == id }?.let {
                    _subscriptionState.update { it.copy(isSubscribed = true) }
                }
            }.onMessage {
                _subscriptionState.update { it.copy(hasMoved = false) }
            }.onErrorMap {
                _subscriptionState.update { it.copy(hasMoved = false) }
            }
        }
    }

    fun refreshNetwork() {
        _networkStatus.value = ConnectivityStatus.CONNECTED
    }

    fun refreshData() {
        loadPaymentTypes()
    }
}

data class PaymentState(
    val isLoading: Boolean = false,
    val payments: List<PaymentTypes> = listOf(),
    val errorMessage: UniversalText = UniversalText.Empty
)

data class SubscriptionState(
    val isLoading: Boolean = false,
    val successResponse: SubscriptionBuyResponse? = null,
    val showError: Boolean = false,
    val errorMessage: UniversalText = UniversalText.Empty,
    val isSubscribed: Boolean = false,
    val hasMoved: Boolean = false
)