package uz.mobile.joybox.data.repository

import kotlinx.coroutines.flow.Flow
import uz.mobile.joybox.domain.model.Movie
import uz.mobile.joybox.domain.model.Tag
import uz.mobile.joybox.domain.util.ResultData

interface SearchRepository {

    suspend fun getTags(): Flow<ResultData<List<Tag>>>

    suspend fun searchMovie(query: String, categoryId: Int?, page: Int): Flow<ResultData<List<Movie>>>

}