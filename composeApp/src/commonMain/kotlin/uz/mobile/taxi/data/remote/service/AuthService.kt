package uz.mobile.taxi.data.remote.service

import uz.mobile.taxi.data.remote.dto.LoginDTO
import uz.mobile.taxi.data.remote.dto.base.Response
import uz.mobile.taxi.domain.model.User

interface AuthService {
    suspend fun login(phone: String, password: String): Response<LoginDTO?>
    suspend fun register(user: User, otp: String): Response<LoginDTO?>
    suspend fun sendSms(phone: String): Response<Any>
    suspend fun passwordSms(phone: String): Response<Any>
    suspend fun passwordReset(user: User, otp: String): Response<Any>
    suspend fun clearToken()
    suspend fun checkTokenIsValid(): Response<LoginDTO?>
}