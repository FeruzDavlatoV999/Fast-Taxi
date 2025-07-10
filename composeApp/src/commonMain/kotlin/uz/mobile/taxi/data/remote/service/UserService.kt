package uz.mobile.taxi.data.remote.service

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
import uz.mobile.taxi.data.remote.dto.base.Response
import uz.mobile.taxi.domain.model.BuySubscriptionRequest
import uz.mobile.taxi.domain.model.WatchInfo

interface UserService {

    suspend fun getUser(): Response<UserResponse>

    suspend fun getLiveUrl(): Response<LiveResponse>

    suspend fun getLiveDates(): Response<LiveDatesResponse>

    suspend fun getProgramLive(page: Int, date:String): Response<ProgramResponse>

    suspend fun getCategories(page: Int,isMovie: Boolean, slug: String?, showOnHomepage: Boolean?): Response<CategoryResponse>

    suspend fun getCategories(isMovie: Boolean, slug: String?, showOnHomepage: Boolean?): Response<CategoryResponse>

    suspend fun getMovies(page: Int, id: Int): Response<MoviesResponse>

    suspend fun getMovieDetail(id: Int): Response<MovieDetailResponse>

    suspend fun getTags(): Response<TagsResponse>

    suspend fun getPaymentTypes(type:String): Response<PaymentSystemsResponse>

    suspend fun getHistoryMovies(): Response<ShowedListResponse>

    suspend fun getSettings(): Response<GeneralSettings>

    suspend fun getSubscriptions(): Response<SubscriptionsResponse>

    suspend fun buySubscription(data: BuySubscriptionRequest): Response<SubscriptionBuyResponse>

    suspend fun getSubscriptions(mySubscriptions:Boolean): Response<SubscriptionsResponse>

    suspend fun setMovieInfo(watchInfo: WatchInfo): Response<Any>

    suspend fun getBanners(page: Int): Response<BannersResponse>

    suspend fun getBalance(): Response<BalanceResponse>

    suspend fun payBalance(amount:String,paymentAlias:String): Response<PayBalanceResponse>

    suspend fun promoCode(code:String): Response<PromoCodeResponse>

    suspend fun updateProfile(
        firstName: String,
        gender: String,
        birthday: String,
        avatarFile: ByteArray?,
        avatarMimeType: String?,
    ): Response<UpdateProfileResponse>
}