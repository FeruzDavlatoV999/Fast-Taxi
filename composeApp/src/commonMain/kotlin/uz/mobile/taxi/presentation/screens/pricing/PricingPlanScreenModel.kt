package uz.mobile.taxi.presentation.screens.pricing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.mapper.toSubsList
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.domain.model.Subscription


class PricingPlanScreenModel(
    private val repository: MovieRepository
) : ScreenModel {


    var plans: List<Subscription> by mutableStateOf(emptyList())
        private set

    var selectedPlan: Subscription? by mutableStateOf(null)
        private set


    fun selectedId(id: Int) {
        selectedPlan = plans.find { it.id == id }
    }


    init {
        getSubscriptions()
    }


    fun getSubscriptions() = screenModelScope.launch(Dispatchers.IO) {
        repository.getSubscriptions().collect { result ->
            result.onSuccess { data ->
                val list = data?.subscriptions?.toSubsList().orEmpty()
                selectedPlan = list.firstOrNull()
                plans = list
            }.onLoading {

            }.onMessage {

            }
        }
    }


}


sealed interface PlanState {
    data object Success : PlanState
    data object Loading : PlanState
    data object Idl : PlanState
    data class Error(val error: String) : PlanState
}