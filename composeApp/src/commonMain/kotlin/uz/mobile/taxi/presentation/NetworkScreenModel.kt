package uz.mobile.taxi.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import chaintech.network.connectivitymonitor.ConnectivityMonitor
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NetworkScreenModel : ScreenModel {

    private val _connectivityStatus = MutableStateFlow(ConnectivityStatus.DETERMINING)
    val connectivityStatus: StateFlow<ConnectivityStatus> = _connectivityStatus.asStateFlow()

    private var viewModelJob: Job? = null

    init {
        startMonitoring()
    }

    fun startMonitoring() {
        viewModelJob?.cancel()
        viewModelJob = CoroutineScope(Dispatchers.Default).launch {
            ConnectivityMonitor.instance.status.collect { newStatus ->
                _connectivityStatus.value = newStatus
            }
        }
    }

    fun stopMonitoring() {
        viewModelJob?.cancel()
        ConnectivityMonitor.instance.stopMonitoring()
    }

    fun refresh() {
        ConnectivityMonitor.instance.refresh()
    }

}
