package uz.mobile.joybox.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NotificationsResponseDTO(
   @SerialName("notifications") val notifications:List<NotificationDTO>,
   @SerialName("paginator") val paginator:Paginator?,

)

@Serializable
data class NotificationDTO(
   @SerialName("id") val id:Int?,
   @SerialName("title") val title:String?,
   @SerialName("data") val data:String?,
   @SerialName("image") val image:String?,
   @SerialName("created_at") val createdAt:String
)
