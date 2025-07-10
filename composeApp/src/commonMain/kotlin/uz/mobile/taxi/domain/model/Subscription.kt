package uz.mobile.taxi.domain.model

data class Subscription(
    val id:Int,
    val name:String,
    val description:String,
    val price:Long,
    val oldPrice:Long,
    val currency:String,
    val bestSeller:Boolean
)
