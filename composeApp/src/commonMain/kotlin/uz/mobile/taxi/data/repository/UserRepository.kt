package uz.mobile.taxi.data.repository

import kotlinx.coroutines.flow.Flow
import uz.mobile.taxi.data.remote.dto.GeneralSettings
import uz.mobile.taxi.data.remote.dto.UpdateProfileResponse
import uz.mobile.taxi.domain.model.User
import uz.mobile.taxi.domain.util.ResultData

interface UserRepository {
    suspend fun getSettings(): Flow<ResultData<GeneralSettings?>>
    suspend fun getUser(): Flow<ResultData<User?>>
    suspend fun updateProfile(firstName: String, gender:String, birthday:String, avatarFile: ByteArray?, avatarMimeType: String?,): Flow<ResultData<UpdateProfileResponse?>>
}