package uz.mobile.joybox.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.domain.model.Notification

interface NotificationRepository {

    suspend fun getNotifications(): Flow<PagingData<Notification>>

}