package uz.mobile.taxi.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class CachingManager(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("theme_cache")

    private val key_index = intPreferencesKey("index_key")
    private val key_language = stringPreferencesKey("language")
    private val token_access = stringPreferencesKey("token_access")
    private val token_refresh = stringPreferencesKey("token_refresh")
    private val token_expire = stringPreferencesKey("token_expire")
    private val first_time = booleanPreferencesKey("first_time")
    private val notification = booleanPreferencesKey("notification")

    actual suspend fun saveThemeIndex(index: Int) {
        context.dataStore.edit {
            it[key_index] = index
        }
    }

    actual fun getThemeIndex(): Flow<Int> = context.dataStore.data.map {
        it[key_index] ?: 0
    }

    actual suspend fun saveLanguage(language: String) {
        context.dataStore.edit {
            it[key_language] = language
        }
    }

    actual fun getLanguage(): Flow<String> = context.dataStore.data.map {
        it[key_language] ?: "uz"
    }

    actual suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit {
            it[token_refresh] = token
        }
    }

    actual fun getRefreshToken(): Flow<String> = context.dataStore.data.map {
        it[token_refresh] ?: ""
    }

    actual suspend fun saveAccessToken(token: String) {
        context.dataStore.edit {
            it[token_access] = token
        }
    }

    actual fun getAccessToken(): Flow<String> = context.dataStore.data.map {
        it[token_access] ?: ""
    }

    actual suspend fun saveExpireToken(expireToken: String) {
        context.dataStore.edit {
            it[token_expire] = expireToken
        }
    }

    actual fun getExpireToken(): Flow<String> = context.dataStore.data.map {
        it[token_expire] ?: ""
    }

    actual suspend fun saveFirstTime(isFirstTime: Boolean) {
        context.dataStore.edit {
            it[first_time] = isFirstTime
        }
    }

    actual fun getFirstTime(): Flow<Boolean> = context.dataStore.data.map {
        it[first_time] ?: false
    }

    actual suspend fun saveNotification(isFirstTime: Boolean) {
        context.dataStore.edit {
            it[notification] = isFirstTime
        }
    }

    actual fun getNotification(): Flow<Boolean> = context.dataStore.data.map {
        it[notification] ?: false
    }

}