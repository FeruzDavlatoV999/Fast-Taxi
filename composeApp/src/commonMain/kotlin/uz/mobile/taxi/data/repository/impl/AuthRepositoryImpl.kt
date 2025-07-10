package uz.mobile.taxi.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import uz.mobile.taxi.data.remote.service.AuthService
import uz.mobile.taxi.data.repository.AuthRepository
import uz.mobile.taxi.datastore.CachingManager
import uz.mobile.taxi.domain.model.User
import uz.mobile.taxi.domain.util.ResponseHandler
import uz.mobile.taxi.domain.util.ResultData

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val responseHandler: ResponseHandler,
    private val cache: CachingManager,
) : AuthRepository {


    override suspend fun register(data: User, otp: String): Flow<ResultData<Any?>> = responseHandler.proceed {
        authService.register(data, otp)
    }.map { data ->
        data.map { resultData ->
            val dto = resultData
            val profile = dto?.user?.profile
            val accessToken = dto?.accessToken.orEmpty()
            val firstName = profile?.firstName.orEmpty()
            cache.saveAccessToken(accessToken)
            authService.clearToken()
            User(accessToken = accessToken, firstname = firstName)
        }
    }

    override suspend fun login(phone: String, password: String): Flow<ResultData<User?>> = responseHandler.proceed {
        authService.login(phone, password)
    }.map { data ->
        data.map { resultData ->
            val dto = resultData
            val profile = dto?.user?.profile
            val accessToken = dto?.accessToken.orEmpty()
            val firstName = profile?.firstName.orEmpty()
            cache.saveAccessToken(accessToken)
            authService.clearToken()
            User(accessToken = accessToken, firstname = firstName)
        }
    }


    override suspend fun sendSms(phone: String): Flow<ResultData<Any?>> = responseHandler.proceed {
        authService.sendSms(phone)
    }

    override suspend fun passwordSms(phone: String): Flow<ResultData<Any?>> = responseHandler.proceed {
        authService.passwordSms(phone = phone)
    }

    override suspend fun passwordReset(data: User, otp: String): Flow<ResultData<Any?>> = responseHandler.proceed {
        authService.passwordReset(data, otp)
    }

    override suspend fun isAuthed(): Boolean {
        val accessToken = cache.getAccessToken().firstOrNull()
        return !accessToken.isNullOrBlank()
    }

    override suspend fun logout() {
        cache.saveAccessToken("")
        authService.clearToken()
    }

    override suspend fun checkTokenIsValid(): Flow<ResultData<Any?>> = responseHandler.proceed {
        authService.checkTokenIsValid()
    }.map { data ->
        data.map { resultData ->
            val dto = resultData
            val profile = dto?.user?.profile
            val accessToken = dto?.accessToken.orEmpty()
            val firstName = profile?.firstName.orEmpty()
            cache.saveAccessToken(accessToken)
            authService.clearToken()
            User(accessToken = accessToken, firstname = firstName)
        }
    }


}