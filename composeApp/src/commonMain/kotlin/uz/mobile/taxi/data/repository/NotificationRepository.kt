package uz.mobile.taxi.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.taxi.domain.model.Notification

interface NotificationRepository {

    suspend fun getNotifications(): Flow<PagingData<Notification>>

}