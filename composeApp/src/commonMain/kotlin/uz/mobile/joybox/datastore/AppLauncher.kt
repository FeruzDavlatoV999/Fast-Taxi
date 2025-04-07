package uz.mobile.joybox.datastore


expect class AppLauncher {
    suspend fun getUrl(url: String)
    suspend fun shareProductLink(share:String)
}
