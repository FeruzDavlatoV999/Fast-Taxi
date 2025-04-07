package uz.mobile.joybox.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.data.remote.dto.paging.NotificationsPagingDataSource
import uz.mobile.joybox.data.remote.service.NotificationService
import uz.mobile.joybox.data.repository.NotificationRepository
import uz.mobile.joybox.domain.model.Notification

class NotificationRepositoryImpl(
    private val service: NotificationService
) : NotificationRepository {


    override suspend fun getNotifications(): Flow<PagingData<Notification>> {
       return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NotificationsPagingDataSource(service) }
        ).flow
    }



}