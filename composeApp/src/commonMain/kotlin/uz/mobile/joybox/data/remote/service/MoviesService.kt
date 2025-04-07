package uz.mobile.joybox.data.remote.service

import uz.mobile.joybox.data.remote.dto.CommentsResponse
import uz.mobile.joybox.data.remote.dto.MoviesResponse
import uz.mobile.joybox.data.remote.dto.TagsResponse
import uz.mobile.joybox.data.remote.dto.base.Response
import uz.mobile.joybox.domain.model.Type

interface MoviesService {

    suspend fun searchMovie(query: String, categoryId: Int?, page: Int): Response<MoviesResponse>

    suspend fun getTags(): Response<TagsResponse>

    suspend fun getComment(id: Int, page: Int = 1): Response<CommentsResponse>

    suspend fun postComment(movieId: Int, rating: Float, comment: String, type: Type): Response<Any>

}