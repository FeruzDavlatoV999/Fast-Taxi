package uz.mobile.joybox.data.remote.service

import uz.mobile.joybox.data.remote.dto.BalanceResponse
import uz.mobile.joybox.data.remote.dto.BannersResponse
import uz.mobile.joybox.data.remote.dto.CategoryResponse
import uz.mobile.joybox.data.remote.dto.GeneralSettings
import uz.mobile.joybox.data.remote.dto.LiveDatesResponse
import uz.mobile.joybox.data.remote.dto.LiveResponse
import uz.mobile.joybox.data.remote.dto.MovieDetailResponse
import uz.mobile.joybox.data.remote.dto.MoviesResponse
import uz.mobile.joybox.data.remote.dto.PayBalanceResponse
import uz.mobile.joybox.data.remote.dto.PaymentSystemsResponse
import uz.mobile.joybox.data.remote.dto.ProgramResponse
import uz.mobile.joybox.data.remote.dto.PromoCodeResponse
import uz.mobile.joybox.data.remote.dto.ShowedListResponse
import uz.mobile.joybox.data.remote.dto.SubscriptionBuyResponse
import uz.mobile.joybox.data.remote.dto.SubscriptionsResponse
import uz.mobile.joybox.data.remote.dto.TagsResponse
import uz.mobile.joybox.data.remote.dto.UpdateProfileResponse
import uz.mobile.joybox.data.remote.dto.UserResponse
import uz.mobile.joybox.data.remote.dto.base.Response
import uz.mobile.joybox.domain.model.BuySubscriptionRequest
import uz.mobile.joybox.domain.model.WatchInfo

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