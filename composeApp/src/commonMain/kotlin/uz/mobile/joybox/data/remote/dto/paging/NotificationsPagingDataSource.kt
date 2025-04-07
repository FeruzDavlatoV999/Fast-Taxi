package uz.mobile.joybox.data.remote.dto.paging

import androidx.paging.PagingSource
import app.cash.paging.PagingState
import uz.mobile.joybox.data.remote.mapper.toNotifications
import uz.mobile.joybox.data.remote.service.NotificationService
import uz.mobile.joybox.domain.model.Notification


class NotificationsPagingDataSource(
    private val service: NotificationService
) : PagingSource<Int, Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        return try {
            val currentPage = params.key ?: 1

            val response = service.getNotifications(currentPage)
            val notifications = response.data?.notifications?.toNotifications().orEmpty()

            LoadResult.Page(
                data =  notifications,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.data?.paginator?.nextPageUrl != null) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition
    }
}