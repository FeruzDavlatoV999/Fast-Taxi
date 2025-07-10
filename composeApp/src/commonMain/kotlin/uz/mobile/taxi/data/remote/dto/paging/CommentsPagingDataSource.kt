package uz.mobile.taxi.data.remote.dto.paging

import androidx.paging.PagingSource
import app.cash.paging.PagingState
import uz.mobile.taxi.data.remote.mapper.toComments
import uz.mobile.taxi.data.remote.service.MoviesService
import uz.mobile.taxi.domain.model.Comment


class CommentsPagingDataSource(
    private val service: MoviesService,
    private val id: Int
) : PagingSource<Int, Comment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            val currentPage = params.key ?: 1

            val commentResponse = service.getComment(id, currentPage)
            val comments = commentResponse.data?.userLessonComments?.toComments().orEmpty()

            LoadResult.Page(
                data = comments ?: emptyList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (commentResponse.data?.paginator?.nextPageUrl != null) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition
    }
}