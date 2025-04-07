package uz.mobile.joybox.data.remote.service.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import uz.mobile.joybox.data.remote.UserEndpoints
import uz.mobile.joybox.data.remote.dto.NotificationsResponseDTO
import uz.mobile.joybox.data.remote.dto.base.BaseResponse
import uz.mobile.joybox.data.remote.dto.base.Response
import uz.mobile.joybox.data.remote.service.NotificationService

class NotificationServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : NotificationService {

    override suspend fun getNotifications(page: Int): Response<NotificationsResponseDTO> {
        val response = httpClient.get(baseUrl + UserEndpoints.NOTIFICATIONS) {
            parameter("page", page)
        }
        val data = response.body<BaseResponse<NotificationsResponseDTO?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

}