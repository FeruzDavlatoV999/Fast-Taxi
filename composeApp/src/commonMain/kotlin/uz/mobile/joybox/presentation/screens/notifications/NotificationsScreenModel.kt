package uz.mobile.joybox.presentation.screens.notifications

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.repository.NotificationRepository
import uz.mobile.joybox.domain.model.Notification

class NotificationsScreenModel(
    private val notificationRepository: NotificationRepository
) : ScreenModel {


    private val _notificationsFlow = MutableStateFlow<PagingData<Notification>>(PagingData.empty())
    val notificationsFlow = _notificationsFlow.asStateFlow()


    init {
        getNotifications()
    }

    fun getNotifications() = screenModelScope.launch(Dispatchers.IO) {
        notificationRepository.getNotifications()
            .cachedIn(screenModelScope)
            .onEach {
                _notificationsFlow.emit(it)
            }.launchIn(screenModelScope)
    }

}