package uz.mobile.taxi.data.remote.dto.paging

import androidx.paging.PagingSource
import uz.mobile.taxi.data.remote.dto.BannersResponse
import uz.mobile.taxi.data.remote.service.UserService


class HomeBannersPagingDataSource (
    private val userService: UserService,
) : PagingSource<Int, BannersResponse.Banner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BannersResponse.Banner> {
        return try {
            val currentPage = params.key ?: 1

            val courseResponse = userService.getBanners(currentPage)

            LoadResult.Page(
                data = courseResponse.data?.banners ?: emptyList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (courseResponse.data?.paginator?.nextPageUrl != null) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, BannersResponse.Banner>): Int? {
        return state.anchorPosition
    }
}