package uz.mobile.joybox.data.remote.dto.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<out T>(
    @SerialName("data") val data: T?,
    @SerialName("message") val message: String?,
    @SerialName("status") val status: String?,
    @SerialName("errors") val errors: Map<String, List<String>>?,
)


data class Response<out T>(
    val data: T?,
    val message: String,
    val status: Int,
    val isSuccess: Boolean,
    val errors: Map<String, List<String>>
)


