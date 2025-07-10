package uz.mobile.taxi.data.remote.service.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject
import uz.mobile.taxi.data.remote.UserEndpoints
import uz.mobile.taxi.data.remote.dto.BalanceResponse
import uz.mobile.taxi.data.remote.dto.BannersResponse
import uz.mobile.taxi.data.remote.dto.CategoryResponse
import uz.mobile.taxi.data.remote.dto.GeneralSettings
import uz.mobile.taxi.data.remote.dto.LiveDatesResponse
import uz.mobile.taxi.data.remote.dto.LiveResponse
import uz.mobile.taxi.data.remote.dto.MovieDetailResponse
import uz.mobile.taxi.data.remote.dto.MoviesResponse
import uz.mobile.taxi.data.remote.dto.PayBalanceResponse
import uz.mobile.taxi.data.remote.dto.PaymentSystemsResponse
import uz.mobile.taxi.data.remote.dto.ProgramResponse
import uz.mobile.taxi.data.remote.dto.PromoCodeResponse
import uz.mobile.taxi.data.remote.dto.ShowedListResponse
import uz.mobile.taxi.data.remote.dto.SubscriptionBuyResponse
import uz.mobile.taxi.data.remote.dto.SubscriptionsResponse
import uz.mobile.taxi.data.remote.dto.TagsResponse
import uz.mobile.taxi.data.remote.dto.UpdateProfileResponse
import uz.mobile.taxi.data.remote.dto.UserResponse
import uz.mobile.taxi.data.remote.dto.base.BaseResponse
import uz.mobile.taxi.data.remote.dto.base.Response
import uz.mobile.taxi.data.remote.service.UserService
import uz.mobile.taxi.domain.model.BuySubscriptionRequest
import uz.mobile.taxi.domain.model.WatchInfo

class UserServiceImpl(private val httpClient: HttpClient, private val baseUrl: String) :
    UserService {

    override suspend fun getUser(): Response<UserResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_USER)

        val data = response.body<BaseResponse<UserResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getLiveUrl(): Response<LiveResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_LIVE_URL)

        val data = response.body<BaseResponse<LiveResponse?>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getLiveDates(): Response<LiveDatesResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_LIVE_DATES)

        val data = response.body<BaseResponse<LiveDatesResponse?>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getProgramLive(page: Int, date: String): Response<ProgramResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_LIVE_PROGRAM) {
            parameter("page", page)
            parameter("date", date)
        }

        val data = response.body<BaseResponse<ProgramResponse?>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getCategories(
        page: Int,
        isMovie: Boolean,
        slug: String?,
        showOnHomepage: Boolean?
    ): Response<CategoryResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_CATEGORIES) {
            parameter("page", page)
            parameter("courses", isMovie)
            parameter("byTagSlug", slug)
            parameter("showOnHomepage", showOnHomepage)
        }

        val data = response.body<BaseResponse<CategoryResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty(),
        )
    }

    override suspend fun getCategories(
        isMovie: Boolean,
        slug: String?,
        showOnHomepage: Boolean?
    ): Response<CategoryResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_CATEGORIES) {
            parameter("courses", isMovie)
            parameter("byTagSlug", slug)
            parameter("showOnHomepage", showOnHomepage)
        }

        val data = response.body<BaseResponse<CategoryResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getMovies(page: Int, id: Int): Response<MoviesResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_MOVIES) {
            parameter("page", page)
            parameter("byCategoryId", id)
        }

        val data = response.body<BaseResponse<MoviesResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getMovieDetail(id: Int): Response<MovieDetailResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_MOVIES + "/$id")
        val data = response.body<BaseResponse<MovieDetailResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getTags(): Response<TagsResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_TAGS)
        val data = response.body<BaseResponse<TagsResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getPaymentTypes(type: String): Response<PaymentSystemsResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.PAYMENT_TYPES) {
            parameter("type", type)
        }
        val data = response.body<BaseResponse<PaymentSystemsResponse>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getHistoryMovies(): Response<ShowedListResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_HISTORY_MOVIES)
        val data = response.body<BaseResponse<ShowedListResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getSettings(): Response<GeneralSettings> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_SETTINGS)
        val data = response.body<BaseResponse<GeneralSettings?>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getSubscriptions(): Response<SubscriptionsResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_SUBSCRIPTION)
        val data = response.body<BaseResponse<SubscriptionsResponse>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getSubscriptions(mySubscriptions: Boolean): Response<SubscriptionsResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_SUBSCRIPTION) {
            parameter("mySubscriptions", mySubscriptions)
        }
        val data = response.body<BaseResponse<SubscriptionsResponse>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun buySubscription(buySubscription: BuySubscriptionRequest): Response<SubscriptionBuyResponse> {
        val response = httpClient.post(baseUrl + UserEndpoints.BUY_SUBSCRIPTION) {
            parameter("product_id", buySubscription.productId)
            parameter("product_type", buySubscription.productType)
            parameter("payment_alias", buySubscription.paymentAlias)
            parameter("promo_code", buySubscription.promCode)
        }
        val data = response.body<BaseResponse<SubscriptionBuyResponse>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun setMovieInfo(watchInfo: WatchInfo): Response<Any> {

        val response = httpClient.post(baseUrl + UserEndpoints.SET_WATCH_INFO) {
            parameter("watchable_id", watchInfo.watchableId)
            parameter("watchable_type", watchInfo.watchableType)
            parameter("watched_duration", watchInfo.watchedDuration)
        }

        val data = response.body<BaseResponse<JsonObject?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getBanners(page: Int): Response<BannersResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_BANNERS) {
            parameter("page", page)
        }
        val data = response.body<BaseResponse<BannersResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getBalance(): Response<BalanceResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_BALANCE)
        val data = response.body<BaseResponse<BalanceResponse?>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun payBalance(
        amount: String,
        paymentAlias: String
    ): Response<PayBalanceResponse> {
        val response = httpClient.post(baseUrl + UserEndpoints.PAY_BALANCE) {
            parameter("amount", amount)
            parameter("payment_alias", paymentAlias)
        }
        val data = response.body<BaseResponse<PayBalanceResponse>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun promoCode(code: String): Response<PromoCodeResponse> {
        val response = httpClient.post(baseUrl + UserEndpoints.PROMO_CODE) {
            parameter("code", code)
        }
        val data = response.body<BaseResponse<PromoCodeResponse?>>()
        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun updateProfile(
        firstName: String,
        gender: String,
        birthday: String,
        avatarFile: ByteArray?,
        avatarMimeType: String?,
    ): Response<UpdateProfileResponse> {
        val response: HttpResponse = httpClient.submitFormWithBinaryData(
            url = baseUrl + UserEndpoints.UPDATE_PROFILE,
            formData = formData {
                append("first_name", firstName)
                append("gender", gender)
                append("birthday", birthday)
                avatarFile?.let {
                    append(
                        "avatar",
                        it,
                        Headers.build {
                            append(
                                HttpHeaders.ContentType,
                                avatarMimeType ?: "application/octet-stream"
                            ) // Use the provided MIME type or default
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"avatar.${getFileExtensionFromMimeType(avatarMimeType)}\""
                            )
                        }
                    )
                }
            }
        )
        val data = response.body<BaseResponse<UpdateProfileResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    private fun getFileExtensionFromMimeType(mimeType: String?): String {
        return when (mimeType) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            "image/gif" -> "gif"
            else -> "bin"
        }
    }

}