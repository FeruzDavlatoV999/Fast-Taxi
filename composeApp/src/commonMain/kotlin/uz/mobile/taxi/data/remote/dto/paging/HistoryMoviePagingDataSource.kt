package uz.mobile.taxi.data.remote.dto.paging

import androidx.paging.PagingSource
import uz.mobile.taxi.data.remote.dto.toHistoryMovie
import uz.mobile.taxi.data.remote.service.UserService
import uz.mobile.taxi.domain.model.Movie


class HistoryMoviePagingDataSource (
    private val userService: UserService
) : PagingSource<Int,Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1

            val courseResponse = userService.getHistoryMovies()

            LoadResult.Page(
                data = courseResponse.data?.userWatchHistories?.toHistoryMovie().orEmpty(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (courseResponse.data?.paginator?.nextPageUrl != null) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}