package uz.mobile.joybox.data.remote.mapper

import uz.mobile.joybox.data.remote.dto.NotificationDTO
import uz.mobile.joybox.data.util.formatDate
import uz.mobile.joybox.data.util.formatTime
import uz.mobile.joybox.domain.model.Notification

fun NotificationDTO.toNotification(): Notification = Notification(
    id = id ?: -1,
    title = title.orEmpty(),
    description = data.orEmpty(),
    date = formatDate(createdAt),
    imgUrl = image.orEmpty(),
    time = formatTime(createdAt)
)

fun List<NotificationDTO>.toNotifications(): List<Notification> = this.map { it.toNotification() }