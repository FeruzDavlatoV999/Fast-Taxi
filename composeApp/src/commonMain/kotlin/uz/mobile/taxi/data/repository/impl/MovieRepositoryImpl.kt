package uz.mobile.taxi.data.repository.impl

import androidx.paging.PagingData
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.mobile.taxi.data.remote.dto.BalanceResponse
import uz.mobile.taxi.data.remote.dto.BannersResponse
import uz.mobile.taxi.data.remote.dto.CategoryResponse
import uz.mobile.taxi.data.remote.dto.LiveDatesResponse
import uz.mobile.taxi.data.remote.dto.MovieDetailResponse
import uz.mobile.taxi.data.remote.dto.PayBalanceResponse
import uz.mobile.taxi.data.remote.dto.ProgramResponse
import uz.mobile.taxi.data.remote.dto.PromoCodeResponse
import uz.mobile.taxi.data.remote.dto.SubscriptionBuyResponse
import uz.mobile.taxi.data.remote.dto.SubscriptionsResponse
import uz.mobile.taxi.data.remote.dto.TagsResponse
import uz.mobile.taxi.data.remote.dto.paging.CommentsPagingDataSource
import uz.mobile.taxi.data.remote.dto.paging.HistoryMoviePagingDataSource
import uz.mobile.taxi.data.remote.dto.paging.HomeBannersPagingDataSource
import uz.mobile.taxi.data.remote.dto.paging.HomeCategoryPagingDataSource
import uz.mobile.taxi.data.remote.dto.paging.HomeMoviesPagingdataSource
import uz.mobile.taxi.data.remote.dto.paging.ProgramLivePagingDataSource
import uz.mobile.taxi.data.remote.mapper.toComments
import uz.mobile.taxi.data.remote.service.MoviesService
import uz.mobile.taxi.data.remote.service.UserService
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.domain.model.BuySubscriptionRequest
import uz.mobile.taxi.domain.model.Comment
import uz.mobile.taxi.domain.model.Type
import uz.mobile.taxi.domain.model.Live
import uz.mobile.taxi.domain.model.Movie
import uz.mobile.taxi.domain.model.PaymentTypes
import uz.mobile.taxi.domain.model.WatchInfo
import uz.mobile.taxi.domain.util.ResponseHandler
import uz.mobile.taxi.domain.util.ResultData

class MovieRepositoryImpl(
    private val service: UserService,
    private val moviesService: MoviesService,
    private val responseHandler: ResponseHandler,
) : MovieRepository {

    override suspend fun getTags(): Flow<ResultData<TagsResponse?>> =
        responseHandler.proceed { service.getTags() }

    override suspend fun getBanners(): Flow<PagingData<BannersResponse.Banner>> {
        return Pager(
            config = PagingConfig(pageSize = 15, prefetchDistance = 10),
            pagingSourceFactory = { HomeBannersPagingDataSource(service) }
        ).flow
    }


    override suspend fun getCategories(
        isMovie: Boolean,
        slug: String?,
        showOnHomepage: Boolean?,
    ): Flow<PagingData<CategoryResponse.Category>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                HomeCategoryPagingDataSource(
                    service,
                    isMovie,
                    slug,
                    showOnHomepage
                )
            }
        ).flow
    }

    override suspend fun getMovies(id: Int): Flow<PagingData<MovieDetailResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { HomeMoviesPagingdataSource(service, id) }
        ).flow
    }

    override suspend fun getMovieDetail(id: Int): Flow<ResultData<MovieDetailResponse?>> =
        responseHandler.proceed { service.getMovieDetail(id) }

    override suspend fun getCategoryWithMovies(
        isMovie: Boolean,
        slug: String?,
        showOnHomepage: Boolean?,
    ): Flow<ResultData<CategoryResponse?>> =
        responseHandler.proceed {
            service.getCategories(isMovie, slug, showOnHomepage)
        }

    override suspend fun getPaymentTypes(type: String): Flow<ResultData<List<PaymentTypes>?>> =
        responseHandler.proceed {
            service.getPaymentTypes(type)
        }.map { data ->
            data.map { response ->
                response?.paymentType?.map { item ->
                    PaymentTypes(
                        alias = item.alias.orEmpty(),
                        logo = item.logo.orEmpty(),
                        name = item.name.orEmpty(),
                        providerName = item.providerName.orEmpty(),
                        providerType = item.providerType.orEmpty(),
                        amount = item.balance
                    )
                }
            }
        }


    override suspend fun getSubscriptions(): Flow<ResultData<SubscriptionsResponse?>> =
        responseHandler.proceed {
            service.getSubscriptions()
        }

    override suspend fun getMySubscriptions(mySubscriptions: Boolean): Flow<ResultData<SubscriptionsResponse?>> =
        responseHandler.proceed {
            service.getSubscriptions(mySubscriptions)
        }

    override suspend fun buySubscription(mySubscriptions: BuySubscriptionRequest): Flow<ResultData<SubscriptionBuyResponse?>> =
        responseHandler.proceed {
            service.buySubscription(mySubscriptions)
        }

    override suspend fun getBalance(): Flow<ResultData<BalanceResponse?>> =
        responseHandler.proceed {
            service.getBalance()
        }

    override suspend fun getComment(id: Int): Flow<ResultData<List<Comment>>> =
        responseHandler.proceed {
            moviesService.getComment(id)
        }.map { it.map { it?.userLessonComments?.toComments().orEmpty() } }

    override suspend fun getCommentPaging(id: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 15),
            pagingSourceFactory = { CommentsPagingDataSource(moviesService, id) }
        ).flow
    }

    override suspend fun postComment(
        id: Int,
        rating: Float,
        comment: String
    ): Flow<ResultData<Any?>> = responseHandler.proceed {
        moviesService.postComment(id, rating, comment, Type.MOVIE)
    }

    override suspend fun promoCode(code: String): Flow<ResultData<PromoCodeResponse?>> =
        responseHandler.proceed {
            service.promoCode(code)
        }

    override suspend fun payBalance(
        amount: String,
        paymentAlias: String,
    ): Flow<ResultData<PayBalanceResponse?>> =
        responseHandler.proceed {
            service.payBalance(amount, paymentAlias)
        }

    override suspend fun getLiveUrl(): Flow<ResultData<Live?>> = responseHandler.proceed {
        service.getLiveUrl()
    }.map { data ->
        data.map { response ->
            val live = response?.liveUrl
            Live(
                id = live?.id ?: 0,
                url = live?.url ?: ""
            )
        }
    }

    override suspend fun getLiveDates(): Flow<ResultData<LiveDatesResponse?>> =
        responseHandler.proceed {
            service.getLiveDates()
        }

    override suspend fun getHistoryMovie(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 15, prefetchDistance = 10),
            pagingSourceFactory = { HistoryMoviePagingDataSource(service) }
        ).flow
    }

    override suspend fun setWatchInfo(watchInfo: WatchInfo): Flow<ResultData<Any?>> =
        responseHandler.proceed {
            service.setMovieInfo(watchInfo)
        }

    override suspend fun getProgram(date: String): Flow<PagingData<ProgramResponse.ScheduleProgram>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 15),
            pagingSourceFactory = { ProgramLivePagingDataSource(service, date) }
        ).flow
    }

}