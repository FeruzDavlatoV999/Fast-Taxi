package uz.mobile.joybox.data.remote.service

import uz.mobile.joybox.data.remote.dto.NotificationsResponseDTO
import uz.mobile.joybox.data.remote.dto.base.Response

interface NotificationService {

    suspend fun getNotifications(page: Int = 1): Response<NotificationsResponseDTO>

}