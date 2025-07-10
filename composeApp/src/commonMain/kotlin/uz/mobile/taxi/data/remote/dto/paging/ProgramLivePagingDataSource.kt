package uz.mobile.taxi.data.remote.dto.paging

import androidx.paging.PagingSource
import uz.mobile.taxi.data.remote.dto.ProgramResponse
import uz.mobile.taxi.data.remote.service.UserService


class ProgramLivePagingDataSource (
    private val userService: UserService,
    private val date:String
) : PagingSource<Int, ProgramResponse.ScheduleProgram>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProgramResponse.ScheduleProgram> {
        return try {
            val currentPage = params.key ?: 1

            val courseResponse = userService.getProgramLive(currentPage,date)

            LoadResult.Page(
                data = courseResponse.data?.schedulePrograms ?: emptyList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (courseResponse.data?.paginator?.nextPageUrl != null) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, ProgramResponse.ScheduleProgram>): Int? {
        return state.anchorPosition
    }
}