package uz.mobile.joybox.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.data.remote.dto.BalanceResponse
import uz.mobile.joybox.data.remote.dto.BannersResponse
import uz.mobile.joybox.data.remote.dto.CategoryResponse
import uz.mobile.joybox.data.remote.dto.LiveDatesResponse
import uz.mobile.joybox.data.remote.dto.MovieDetailResponse
import uz.mobile.joybox.data.remote.dto.PayBalanceResponse
import uz.mobile.joybox.data.remote.dto.ProgramResponse
import uz.mobile.joybox.data.remote.dto.PromoCodeResponse
import uz.mobile.joybox.data.remote.dto.SubscriptionBuyResponse
import uz.mobile.joybox.data.remote.dto.SubscriptionsResponse
import uz.mobile.joybox.data.remote.dto.TagsResponse
import uz.mobile.joybox.domain.model.BuySubscriptionRequest
import uz.mobile.joybox.domain.model.Comment
import uz.mobile.joybox.domain.model.Live
import uz.mobile.joybox.domain.model.Movie
import uz.mobile.joybox.domain.model.PaymentTypes
import uz.mobile.joybox.domain.model.WatchInfo
import uz.mobile.joybox.domain.util.ResultData

interface MovieRepository {
    suspend fun getTags(): Flow<ResultData<TagsResponse?>>
    suspend fun getBanners(): Flow<PagingData<BannersResponse.Banner>>
    suspend fun getCategories(isMovie: Boolean, slug: String?, showOnHomepage: Boolean?): Flow<PagingData<CategoryResponse.Category>>
    suspend fun getMovies(id: Int): Flow<PagingData<MovieDetailResponse>>
    suspend fun getMovieDetail(id: Int): Flow<ResultData<MovieDetailResponse?>>
    suspend fun getCategoryWithMovies(isMovie: Boolean, slug: String?, showOnHomepage: Boolean?): Flow<ResultData<CategoryResponse?>>
    suspend fun getLiveUrl(): Flow<ResultData<Live?>>
    suspend fun getLiveDates(): Flow<ResultData<LiveDatesResponse?>>
    suspend fun getHistoryMovie(): Flow<PagingData<Movie>>
    suspend fun setWatchInfo(watchInfo: WatchInfo): Flow<ResultData<Any?>>
    suspend fun getProgram(date:String): Flow<PagingData<ProgramResponse.ScheduleProgram>>
    suspend fun getPaymentTypes(type:String): Flow<ResultData<List<PaymentTypes>?>>
    suspend fun getSubscriptions(): Flow<ResultData<SubscriptionsResponse?>>
    suspend fun getMySubscriptions(mySubscriptions: Boolean): Flow<ResultData<SubscriptionsResponse?>>
    suspend fun buySubscription(mySubscriptions: BuySubscriptionRequest): Flow<ResultData<SubscriptionBuyResponse?>>
    suspend fun getBalance(): Flow<ResultData<BalanceResponse?>>
    suspend fun promoCode(code:String): Flow<ResultData<PromoCodeResponse?>>
    suspend fun payBalance(amount:String,paymentAlias:String): Flow<ResultData<PayBalanceResponse?>>
    suspend fun getComment(id: Int): Flow<ResultData<List<Comment>>>
    suspend fun getCommentPaging(id: Int): Flow<PagingData<Comment>>
    suspend fun postComment(id: Int, rating: Float, comment: String): Flow<ResultData<Any?>>
}