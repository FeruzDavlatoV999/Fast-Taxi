package uz.mobile.taxi.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.mobile.taxi.data.remote.dto.GeneralSettings
import uz.mobile.taxi.data.remote.dto.UpdateProfileResponse
import uz.mobile.taxi.data.remote.service.UserService
import uz.mobile.taxi.data.repository.UserRepository
import uz.mobile.taxi.domain.model.User
import uz.mobile.taxi.domain.util.ResponseHandler
import uz.mobile.taxi.domain.util.ResultData

class UserRepositoryImpl(
    private val service: UserService,
    private val responseHandler: ResponseHandler,
) : UserRepository {

    override suspend fun getUser(): Flow<ResultData<User?>> = responseHandler.proceed {
        service.getUser()
    }.map { data ->
        data.map { response ->
            val user = response?.user
            User(
                firstname = "${user?.profile?.firstName}",
                email = user?.profile?.email,
                avatar = user?.profile?.avatar,
                gender = user?.profile?.gender,
                birthday = user?.profile?.birthday,
                userId = user?.profile?.userId.toString(),
                phone = user?.phone,
            )
        }
    }

    override suspend fun updateProfile(
        firstName: String,
        gender: String,
        birthday: String,
        avatarFile: ByteArray?,
        avatarMimeType: String?,
    ): Flow<ResultData<UpdateProfileResponse?>> =
        responseHandler.proceed { service.updateProfile(firstName, gender, birthday, avatarFile, avatarMimeType) }

    override suspend fun getSettings(): Flow<ResultData<GeneralSettings?>> =
        responseHandler.proceed { service.getSettings() }

}