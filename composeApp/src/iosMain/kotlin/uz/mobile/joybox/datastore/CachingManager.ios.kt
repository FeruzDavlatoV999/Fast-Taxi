package uz.mobile.joybox.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSUserDefaults

actual class CachingManager() {

    private val userDefault = NSUserDefaults.standardUserDefaults
    actual suspend fun saveThemeIndex(index: Int) {
        userDefault.setInteger(index.toLong(), "theme_index")
    }

    actual fun getThemeIndex(): Flow<Int> = flow {
        val s = userDefault.integerForKey("theme_index")
        emit(s.toInt())
    }

    actual suspend fun saveLanguage(language: String) {
        userDefault.setObject(language, "language")
    }

    actual fun getLanguage(): Flow<String> = flow {
        val s = userDefault.stringForKey("language") ?: "uz"
        emit(s)
    }

    actual suspend fun saveRefreshToken(token: String) {
        userDefault.setObject(token, "token_refresh")
    }

    actual suspend fun saveAccessToken(token: String) {
        userDefault.setObject(token, "token_access")
    }

    actual fun getAccessToken(): Flow<String> = flow {
        val s = userDefault.stringForKey("token_access")
        emit(s.orEmpty())
    }

    actual fun getRefreshToken(): Flow<String> = flow {
        val s = userDefault.stringForKey("token_refresh")
        emit(s.orEmpty())
    }

    actual suspend fun saveExpireToken(expireToken: String) {
        userDefault.setObject(expireToken, "token_expire")
    }

    actual fun getExpireToken(): Flow<String> = flow {
        val s = userDefault.stringForKey("token_expire")
        emit(s.orEmpty())
    }

    actual suspend fun saveFirstTime(isFirstTime: Boolean) {
        userDefault.setObject(isFirstTime, "first_time")
    }

    actual fun getFirstTime(): Flow<Boolean> = flow {
        val s = userDefault.boolForKey("first_time")
        emit(s)
    }

    actual suspend fun saveNotification(isFirstTime: Boolean) {
        userDefault.setObject(isFirstTime, "notification")
    }

    actual fun getNotification(): Flow<Boolean> = flow {
        val s = userDefault.boolForKey("notification")
        emit(s)
    }
}