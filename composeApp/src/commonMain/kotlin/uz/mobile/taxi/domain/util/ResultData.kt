package uz.mobile.taxi.domain.util


sealed class ResultData<out T> {
    data class Success<out T>(val data: T, val message: String) : ResultData<T>()
    data class Error(val errorMap: Map<String, List<String>>) : ResultData<Nothing>()
    data class Message(val message: UniversalText) : ResultData<Nothing>()
    data object Loading : ResultData<Nothing>()
    data object Network : ResultData<Nothing>()

    suspend fun <R> map(transform: suspend (T) -> R): ResultData<R> = when (this) {
        is Success -> Success(transform(data), message)
        is Error -> this
        is Loading -> this
        is Network -> this
        is Message -> this
    }

    fun <R> flatMap(transform: (T) -> ResultData<R>): ResultData<R> = when (this) {
        is Success -> transform(data)
        is Error -> this
        is Loading -> this
        is Network -> this
        is Message -> this
    }

    suspend fun onSuccess(action: suspend (T) -> Unit): ResultData<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }


    suspend fun onErrorMap(action: suspend (map: Map<String, List<String>>) -> Unit): ResultData<T> {
        if (this is Error) {
            action(errorMap)
        }
        return this
    }

    suspend fun onMessage(action: suspend (UniversalText) -> Unit): ResultData<T> {
        if (this is Message) {
            action(message)
        }
        return this
    }

    suspend fun onLoading(action: suspend () -> Unit): ResultData<T> {
        if (this is Loading) {
            action()
        }
        return this
    }

    suspend fun onNetwork(action: suspend () -> Unit): ResultData<T> {
        if (this is Network) {
            action()
        }
        return this
    }
}
