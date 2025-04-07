package uz.mobile.joybox.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WatchInfo(
    val watchableId:Int,
    val watchableType:Type,
    val watchedDuration:Int,
)
