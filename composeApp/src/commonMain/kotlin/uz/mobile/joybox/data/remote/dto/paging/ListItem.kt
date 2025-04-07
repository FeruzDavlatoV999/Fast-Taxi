package uz.mobile.joybox.data.remote.dto.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.data.remote.dto.CategoryResponse
import uz.mobile.joybox.data.remote.dto.MovieDetailResponse

sealed class ListItem {
    data class CategoryWithCourses(
        val category: CategoryResponse.Category,
        val coursePagingData: Flow<PagingData<MovieDetailResponse>>
    ) : ListItem()
}