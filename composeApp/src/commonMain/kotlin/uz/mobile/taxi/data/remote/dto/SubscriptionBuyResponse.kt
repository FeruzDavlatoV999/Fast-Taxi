package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SubscriptionBuyResponse(
    @SerialName("payment_link")
    val paymentLink: String? = null,
    @SerialName("is_free")
    val isFree: Boolean? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("message")
    val message: String? = null,
)
