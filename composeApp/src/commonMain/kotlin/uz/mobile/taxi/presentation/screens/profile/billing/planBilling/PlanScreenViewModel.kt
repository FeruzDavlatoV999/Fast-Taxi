package uz.mobile.taxi.presentation.screens.profile.billing.planBilling

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.dto.SubscriptionsResponse
import uz.mobile.taxi.data.repository.MovieRepository

class PlanScreenViewModel(
    private val movieRepository: MovieRepository
) : ScreenModel {

    private val _getMySubscription= MutableStateFlow<PlanState>(PlanState.Idle)
    val getMySubscription = _getMySubscription.asStateFlow()

    init {
        getMySubscription(true)
    }

    fun getMySubscription(mySubscription:Boolean) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getMySubscriptions(mySubscription).collect { result ->
            result.onSuccess { subscription ->
                subscription.let { items ->
                    _getMySubscription.emit(PlanState.SuccessMySubscription(items))
                }
            }.onLoading {
                _getMySubscription.emit(PlanState.Loading)
            }
        }
    }

    sealed interface PlanState {
        data object Loading : PlanState
        data object Idle : PlanState
        data class SuccessMySubscription(val mySubscription: SubscriptionsResponse?) : PlanState
        data class Error(val error: String) : PlanState
    }
}