package uz.mobile.joybox.data.repository.impl

import kotlinx.coroutines.flow.map
import uz.mobile.joybox.data.remote.dto.toMovies
import uz.mobile.joybox.data.remote.dto.toTags
import uz.mobile.joybox.data.remote.service.MoviesService
import uz.mobile.joybox.data.repository.SearchRepository
import uz.mobile.joybox.domain.util.ResponseHandler

class SearchRepositoryImpl(
    private val userService: MoviesService,
    private val responseHandler: ResponseHandler
) : SearchRepository {

    override suspend fun getTags() = responseHandler.proceed {
        userService.getTags()
    }.map { response ->
        response.map { it?.tags?.toTags().orEmpty() }
    }

    override suspend fun searchMovie(query: String, categoryId: Int?, page: Int) = responseHandler.proceed {
        userService.searchMovie(query, categoryId, page)
    }.map { response ->
        response.map { it?.movies?.toMovies().orEmpty() }
    }

}

