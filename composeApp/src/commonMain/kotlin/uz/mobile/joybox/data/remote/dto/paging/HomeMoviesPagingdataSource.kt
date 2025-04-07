package uz.mobile.joybox.data.remote.dto.paging

import androidx.paging.PagingSource
import app.cash.paging.PagingState
import uz.mobile.joybox.data.remote.dto.MovieDetailResponse
import uz.mobile.joybox.data.remote.service.UserService


class HomeMoviesPagingdataSource (
    private val userService: UserService,
    private val id: Int
) : PagingSource<Int, MovieDetailResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetailResponse> {
        return try {
            val currentPage = params.key ?: 1

            val courseResponse = userService.getMovies(currentPage,id)

            LoadResult.Page(
                data = courseResponse.data?.movies ?: emptyList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (courseResponse.data?.paginator?.nextPageUrl != null) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDetailResponse>): Int? {
        return state.anchorPosition
    }
}