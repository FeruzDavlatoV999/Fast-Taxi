package uz.mobile.taxi.data.remote.mapper

import uz.mobile.taxi.data.remote.dto.NotificationDTO
import uz.mobile.taxi.data.util.formatDate
import uz.mobile.taxi.data.util.formatTime
import uz.mobile.taxi.domain.model.Notification

fun NotificationDTO.toNotification(): Notification = Notification(
    id = id ?: -1,
    title = title.orEmpty(),
    description = data.orEmpty(),
    date = formatDate(createdAt),
    imgUrl = image.orEmpty(),
    time = formatTime(createdAt)
)

fun List<NotificationDTO>.toNotifications(): List<Notification> = this.map { it.toNotification() }