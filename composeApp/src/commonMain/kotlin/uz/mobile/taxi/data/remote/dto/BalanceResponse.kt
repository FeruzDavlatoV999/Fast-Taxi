package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BalanceResponse(
    @SerialName("balance")
    val balance: Balance? = null,
) {
    @Serializable
    data class Balance(
        @SerialName("active")
        val active: Boolean? = null,
        @SerialName("amount")
        val amount: Int? = null,
        @SerialName("currency")
        val currency: String? = null,
        @SerialName("id")
        val id: String? = null,
        @SerialName("isDefault")
        val isDefault: Boolean? = null,
        @SerialName("type")
        val type: String? = null,
    )
}
