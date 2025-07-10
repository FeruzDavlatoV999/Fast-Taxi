package uz.mobile.taxi.datastore


expect class AppLauncher {
    suspend fun getUrl(url: String)
    suspend fun shareProductLink(share:String)
}
