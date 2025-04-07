package uz.mobile.joybox.data.remote.service.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject
import uz.mobile.joybox.data.remote.UserEndpoints
import uz.mobile.joybox.data.remote.dto.CommentsResponse
import uz.mobile.joybox.data.remote.dto.MoviesResponse
import uz.mobile.joybox.data.remote.dto.PostCommentDTO
import uz.mobile.joybox.data.remote.dto.TagsResponse
import uz.mobile.joybox.data.remote.dto.base.BaseResponse
import uz.mobile.joybox.data.remote.dto.base.Response
import uz.mobile.joybox.data.remote.service.MoviesService
import uz.mobile.joybox.domain.model.Type

class MoviesServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : MoviesService {

    override suspend fun searchMovie(query: String, categoryId: Int?, page: Int): Response<MoviesResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_MOVIES) {
            url {
                parameters.append("search", query)
                categoryId?.let { parameters.append("byCategoryId", categoryId.toString()) }
                parameters.append("page", page.toString())
            }
        }
        val data = response.body<BaseResponse<MoviesResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getTags(): Response<TagsResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.GET_TAGS)
        val data = response.body<BaseResponse<TagsResponse?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun getComment(id: Int, page: Int): Response<CommentsResponse> {
        val response = httpClient.get(baseUrl + UserEndpoints.COMMENTS) {
            parameter("model_id", id)
            parameter("type", "movie")
            parameter("page", page)
        }
        val data = response.body<BaseResponse<CommentsResponse?>>()


        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }

    override suspend fun postComment(movieId: Int, rating: Float, comment: String, type: Type): Response<Any> {
        val response = httpClient.post(baseUrl + UserEndpoints.COMMENTS) {
            contentType(ContentType.Application.Json)
            setBody(PostCommentDTO(body = comment, userLessonId = movieId, type = type.name.lowercase(), rate = rating))
        }
        val data = response.body<BaseResponse<JsonObject?>>()

        return Response(
            data = data.data,
            message = data.message.orEmpty(),
            status = response.status.value,
            isSuccess = response.status.isSuccess(),
            errors = data.errors.orEmpty()
        )
    }


}