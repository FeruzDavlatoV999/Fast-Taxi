package uz.mobile.joybox.domain.util

import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.something_went_wrong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.mobile.joybox.data.remote.dto.base.Response

class ResponseHandler {

    suspend fun <T> proceed(
        request: suspend () -> Response<T>,
    ): Flow<ResultData<T?>> = flow {
        emit(ResultData.Loading)

        val response = request()

        if (response.isSuccess) {
            val data = response.data
            val message = response.message
            emit(ResultData.Success(data, message))
        } else {
            emit(onError(response))
        }
    }.catch {
        it.printStackTrace()
        emit(ResultData.Network)
    }

    private fun <T> onError(response: Response<T>): ResultData<T?> {
        return if (response.errors.isNotEmpty()) {
            ResultData.Error(errorMap = response.errors)
        } else if (response.message.isNotEmpty()) {
            ResultData.Message(message = UniversalText.Dynamic(response.message))
        } else {
            ResultData.Message(message = UniversalText.Resource(Res.string.something_went_wrong))
        }
    }

}