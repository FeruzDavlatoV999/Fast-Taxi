package uz.mobile.joybox.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PayBalanceResponse(
    @SerialName("link")
    val link: String? = null,
)
