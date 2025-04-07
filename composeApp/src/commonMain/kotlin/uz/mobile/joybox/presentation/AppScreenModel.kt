package uz.mobile.joybox.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import chaintech.network.connectivitymonitor.ConnectivityMonitor
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.repository.AuthRepository
import uz.mobile.joybox.datastore.CachingManager
import uz.mobile.joybox.datastore.LanguageDataStore

class AppScreenModel(
    private val authRepository: AuthRepository,
    private val cachingManager: CachingManager,
    private val languageDataStore: LanguageDataStore,
) : ScreenModel {

    var stateChecking: Boolean by mutableStateOf(true)
        private set

    var navigate: Navigate by mutableStateOf(Navigate.Idle)
        private set


    init {
        checkAuthorized()
        listenLanguageChange()
        subscribeNotification()
    }

    private fun subscribeNotification() = screenModelScope.launch(Dispatchers.IO) {
        NotifierManager.getPushNotifier().subscribeToTopic("all")
    }

    private fun listenLanguageChange() = screenModelScope.launch {
        cachingManager.getLanguage().collect {
            languageDataStore.setLanguage(it)
        }
    }

    private fun checkTokenIsValid() = screenModelScope.launch(Dispatchers.IO) {
        authRepository.checkTokenIsValid().collect { result ->
            result.onSuccess {
                navigateHome()
                stateChecking = false
            }.onMessage {
                logout()
                stateChecking = false
            }.onErrorMap {
                logout()
                stateChecking = false
            }
        }
    }

    fun checkAuthorized() = screenModelScope.launch(Dispatchers.IO) {
        val isAuthed = authRepository.isAuthed()

        if (isAuthed) {
            checkTokenIsValid()
        } else {
            navigate = Navigate.ONBOARDING
            stateChecking = false
        }
    }

    fun refresh() {
        ConnectivityMonitor.instance.refresh()
    }

    fun navigateHome() {
        navigate = Navigate.HOME
    }

    fun logout() = screenModelScope.launch {
        authRepository.logout()
        navigate = Navigate.ONBOARDING
    }


    fun login() {
        navigate = Navigate.LOGIN
    }

}


sealed class Navigate {
    data object HOME : Navigate()
    data object ONBOARDING : Navigate()
    data object LOGIN : Navigate()
    data object Idle : Navigate()
}

