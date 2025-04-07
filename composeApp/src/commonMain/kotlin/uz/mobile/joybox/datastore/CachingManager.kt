package uz.mobile.joybox.datastore

import kotlinx.coroutines.flow.Flow

expect class CachingManager {

    suspend fun saveThemeIndex(index: Int)
    suspend fun saveLanguage(language: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveAccessToken(token: String)
    suspend fun saveExpireToken(expireToken: String)
    suspend fun saveFirstTime(isFirstTime: Boolean)
    suspend fun saveNotification(isFirstTime: Boolean)

    fun getThemeIndex(): Flow<Int>
    fun getLanguage(): Flow<String>
    fun getAccessToken(): Flow<String>
    fun getRefreshToken(): Flow<String>
    fun getExpireToken(): Flow<String>
    fun getFirstTime(): Flow<Boolean>
    fun getNotification(): Flow<Boolean>

}