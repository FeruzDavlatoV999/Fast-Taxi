package uz.mobile.joybox.data.remote.service.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonElement
import uz.mobile.joybox.data.remote.AuthEndpoints
import uz.mobile.joybox.data.remote.dto.FPUserDTO
import uz.mobile.joybox.data.remote.dto.LoginDTO
import uz.mobile.joybox.data.remote.dto.PhoneDTO
import uz.mobile.joybox.data.remote.dto.UserDTO
import uz.mobile.joybox.data.remote.dto.base.BaseResponse
import uz.mobile.joybox.data.remote.dto.base.Response
import uz.mobile.joybox.data.remote.service.AuthService
import uz.mobile.joybox.domain.model.User

class AuthServiceImpl(private val httpClient: HttpClient, private val baseUrl: String) : AuthService {


    override suspend fun login(phone: String, password: String): Response<LoginDTO?> {

        val response = httpClient.post(baseUrl + AuthEndpoints.LOGIN) {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(phone = phone, password = password))
        }

        val data = response.body<BaseResponse<LoginDTO?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun register(user: User, otp: String): Response<LoginDTO?> {

        val response = httpClient.post(baseUrl + AuthEndpoints.REGISTER) {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(firstName = user.firstname, lastName = user.firstname, phone = user.phone, password = user.password, otpCode = otp, passwordConfirmation = user.password))
        }

        val data = response.body<BaseResponse<LoginDTO?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }


    override suspend fun sendSms(phone: String): Response<Any> {
        val response = httpClient.post(baseUrl + AuthEndpoints.SMS) {
            contentType(ContentType.Application.Json)
            setBody(PhoneDTO(phone))
        }

        val data = response.body<BaseResponse<JsonElement>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun passwordSms(phone: String): Response<Any> {
        val response = httpClient.post(baseUrl + AuthEndpoints.PASSWORD_SMS) {
            contentType(ContentType.Application.Json)
            setBody(PhoneDTO(phone))
        }

        val data = response.body<BaseResponse<JsonElement>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun passwordReset(user: User, otp: String): Response<Any> {
        val response = httpClient.post(baseUrl + AuthEndpoints.PASSWORD_RESET) {
            contentType(ContentType.Application.Json)
            setBody(FPUserDTO(phone = user.phone, password = user.password, code = otp, passwordConfirmation = user.password))
        }

        val data = response.body<BaseResponse<LoginDTO?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun clearToken() {
        httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().firstOrNull()?.clearToken()
    }

    override suspend fun checkTokenIsValid(): Response<LoginDTO?> {
        val response = httpClient.post(baseUrl + AuthEndpoints.REFRESH)

        val data = response.body<BaseResponse<LoginDTO?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

}