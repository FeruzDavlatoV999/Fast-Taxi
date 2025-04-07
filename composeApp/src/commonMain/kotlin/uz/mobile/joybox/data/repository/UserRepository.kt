package uz.mobile.joybox.data.repository

import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.data.remote.dto.GeneralSettings
import uz.mobile.joybox.data.remote.dto.UpdateProfileResponse
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.domain.util.ResultData

interface UserRepository {
    suspend fun getSettings(): Flow<ResultData<GeneralSettings?>>
    suspend fun getUser(): Flow<ResultData<User?>>
    suspend fun updateProfile(firstName: String, gender:String, birthday:String, avatarFile: ByteArray?, avatarMimeType: String?,): Flow<ResultData<UpdateProfileResponse?>>
}