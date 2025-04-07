package uz.mobile.joybox.presentation.screens.comments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.repository.MovieRepository
import uz.mobile.joybox.domain.model.Comment

class CommentsScreenModel(
    private val movieId: Int,
    private val movieRepository: MovieRepository,
) : ScreenModel {


    private val _commentsFlow = MutableStateFlow<PagingData<Comment>>(PagingData.empty())
    val commentsFlow = _commentsFlow.asStateFlow()

    var isLoading by mutableStateOf(true)
        private set

    init {
        fetchComments(movieId)
    }


    fun fetchComments(id: Int) = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getCommentPaging(id)
            .cachedIn(screenModelScope)
            .onEach { data ->
                _commentsFlow.emit(data)
                isLoading = false
            }.launchIn(screenModelScope)
    }

}