package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PaymentSystemsResponse(
    @SerialName("paymentSystems")
    val paymentType: List<PaymentType>? = null,
) {
    @Serializable
    data class PaymentType(
        @SerialName("alias")
        val alias: String? = null,
        @SerialName("logo")
        val logo: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("providerName")
        val providerName: String? = null,
        @SerialName("providerType")
        val providerType: String? = null,
        @SerialName("balance")
        val balance: Int? = null,
    )
}
