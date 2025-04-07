package uz.mobile.joybox.data.repository

import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.domain.util.ResultData

interface AuthRepository {


    suspend fun register(data: User, otp: String): Flow<ResultData<Any?>>
    suspend fun login(phone: String, password: String): Flow<ResultData<User?>>
    suspend fun sendSms(phone: String): Flow<ResultData<Any?>>
    suspend fun passwordSms(phone: String): Flow<ResultData<Any?>>
    suspend fun passwordReset(data: User, otp: String): Flow<ResultData<Any?>>
    suspend fun isAuthed(): Boolean
    suspend fun logout()
    suspend fun checkTokenIsValid(): Flow<ResultData<Any?>>

}