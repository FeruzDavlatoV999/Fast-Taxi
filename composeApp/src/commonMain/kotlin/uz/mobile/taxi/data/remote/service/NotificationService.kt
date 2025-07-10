package uz.mobile.taxi.data.remote.service

import uz.mobile.taxi.data.remote.dto.NotificationsResponseDTO
import uz.mobile.taxi.data.remote.dto.base.Response

interface NotificationService {

    suspend fun getNotifications(page: Int = 1): Response<NotificationsResponseDTO>

}