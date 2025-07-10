package uz.mobile.taxi.domain.model

data class PaymentTypes(
    val alias:String,
    val logo:String,
    val name:String,
    val providerName:String,
    val providerType:String,
    val amount:Int? = null
)