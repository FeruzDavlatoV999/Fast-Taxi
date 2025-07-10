package uz.mobile.taxi.data.repository

import kotlinx.coroutines.flow.Flow
import uz.mobile.taxi.domain.model.Movie
import uz.mobile.taxi.domain.model.Tag
import uz.mobile.taxi.domain.util.ResultData

interface SearchRepository {

    suspend fun getTags(): Flow<ResultData<List<Tag>>>

    suspend fun searchMovie(query: String, categoryId: Int?, page: Int): Flow<ResultData<List<Movie>>>

}