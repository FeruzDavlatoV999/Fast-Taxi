package uz.mobile.joybox.data.remote.dto.paging


import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import uz.mobile.joybox.data.remote.dto.CategoryResponse
import uz.mobile.joybox.data.remote.service.UserService

class HomeCategoryPagingDataSource(
    private val userService: UserService,
    private val isMovie: Boolean,
    private val slug: String?,
    private val showOnHomepage: Boolean?
) : PagingSource<Int, CategoryResponse.Category>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryResponse.Category> {
        return try {
            val currentPage = params.key ?: 1

            val categoryResponse = userService.getCategories(currentPage,isMovie,slug,showOnHomepage)
            LoadResult.Page(
                data = categoryResponse.data?.categories ?: emptyList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey =  if (categoryResponse.data?.paginator?.nextPageUrl != null) currentPage + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CategoryResponse.Category>): Int? {
        return state.anchorPosition
    }
}
