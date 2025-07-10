package uz.mobile.taxi.data.remote.dto.paging

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mobile.taxi.data.remote.dto.MovieDetailResponse

object CourseCache {
    private val courseFlows = mutableMapOf<String, Flow<PagingData<MovieDetailResponse>>>()

    fun getCachedCourses(categorySlug: String): Flow<PagingData<MovieDetailResponse>>? {
        return courseFlows[categorySlug]
    }

    fun cacheCourses(categorySlug: String, courseFlow: Flow<PagingData<MovieDetailResponse>>) {
        courseFlows[categorySlug] = courseFlow
    }
}
