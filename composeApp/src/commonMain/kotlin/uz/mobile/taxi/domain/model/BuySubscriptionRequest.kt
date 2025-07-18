package uz.mobile.taxi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BuySubscriptionRequest(
    val productId:Int?,
    val productType:String?,
    val paymentAlias:String?,
    val promCode:Int?,
)