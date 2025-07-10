package uz.mobile.taxi.domain.model


data class Notification(
    val id:Int,
    val title:String,
    val description:String,
    val date: String,
    val time:String,
    val imgUrl:String
) {
    fun toStringDate():String{
        return StringBuilder().append(date).append(" | ").append(time).toString()
    }
}
