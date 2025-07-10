package uz.mobile.taxi.presentation.screens.detailsScreen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.dto.MovieDetailResponse
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.domain.model.Comment

class DetailsScreenViewModel(
    private val movieId: Int,
    private val movieRepository: MovieRepository
) : ScreenModel {


    private val _getMovieDetailInfo = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val getMovieDetailInfo = _getMovieDetailInfo.asStateFlow()

    var rating: Float by mutableStateOf(0f)
        private set

    var comment: String by mutableStateOf("")
        private set

    var comments: CommentState by mutableStateOf(CommentState.Idle)
        private set

    var post: PostCommentState by mutableStateOf(PostCommentState.Idle)
        private set

    init {
        getDetailInfo(movieId)
        fetchComments(movieId)
    }

    fun getDetailInfo(id: Int) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getMovieDetail(id).collect { result ->
            result.onSuccess { movie ->
                movie?.let { item ->
                    _getMovieDetailInfo.emit(MovieDetailState.Success(item))
                }
            }
        }
    }

    fun onRatingChanged(rating: Float) {
        this.rating = rating
    }

    fun onCommentChanged(comment: String) {
        this.comment = comment
    }


    fun postComment() = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.postComment(movieId, rating, comment).collect { result ->
            result.onSuccess {
                clearComment()
                fetchComments(movieId)
                post = PostCommentState.Success
            }.onLoading {
                post = PostCommentState.Loading
            }
        }
    }


    private fun clearComment() {
        comment = ""
        rating = 0f
    }

    fun fetchComments(id: Int) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getComment(id).collect { result ->
            result.onSuccess { list ->
                comments = CommentState.Comments(list)
            }.onLoading {
                comments = CommentState.Loading
            }.onMessage {
                comments = CommentState.Idle
            }.onErrorMap {
                comments = CommentState.Idle
            }
        }
    }


}

sealed interface MovieDetailState {

    data object Loading : MovieDetailState

    data object Idle : MovieDetailState

    data class Success(val user: MovieDetailResponse) : MovieDetailState

    data class Error(val error: String) : MovieDetailState
}

sealed interface CommentState {
    data object Loading : CommentState
    data object Idle : CommentState
    data class Comments(val comments: List<Comment>) : CommentState
    data class Error(val error: String) : CommentState
}

sealed interface PostCommentState {
    data object Loading : PostCommentState
    data object Idle : PostCommentState
    data object Success : PostCommentState
    data class Error(val error: String) : PostCommentState
}