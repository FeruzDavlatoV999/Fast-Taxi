package uz.mobile.joybox.data.remote.service

import uz.mobile.joybox.data.remote.dto.LoginDTO
import uz.mobile.joybox.data.remote.dto.base.Response
import uz.mobile.joybox.domain.model.User

interface AuthService {
    suspend fun login(phone: String, password: String): Response<LoginDTO?>
    suspend fun register(user: User, otp: String): Response<LoginDTO?>
    suspend fun sendSms(phone: String): Response<Any>
    suspend fun passwordSms(phone: String): Response<Any>
    suspend fun passwordReset(user: User, otp: String): Response<Any>
    suspend fun clearToken()
    suspend fun checkTokenIsValid(): Response<LoginDTO?>
}