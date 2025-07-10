package uz.mobile.taxi.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.taxi.data.remote.dto.paging.NotificationsPagingDataSource
import uz.mobile.taxi.data.remote.service.NotificationService
import uz.mobile.taxi.data.repository.NotificationRepository
import uz.mobile.taxi.domain.model.Notification

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