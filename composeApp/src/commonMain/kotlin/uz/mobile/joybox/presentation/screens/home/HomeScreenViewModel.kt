package uz.mobile.joybox.presentation.screens.home

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.remote.dto.BannersResponse
import uz.mobile.joybox.data.remote.dto.CategoryResponse
import uz.mobile.joybox.data.remote.dto.TagsResponse
import uz.mobile.joybox.data.repository.MovieRepository
import uz.mobile.joybox.domain.model.Movie

class HomeScreenViewModel(
    private val userRepository: MovieRepository,
) : ScreenModel {

    fun launch(){
        getBanners()
        getTags()
        fetchHistoryMovie()
        fetchCategoriesWithCourses(true, showOnHomepage = true)
    }


    var selectedTab: Int by mutableStateOf(0)
        private set

    fun updateTab(tab: Int) {
        selectedTab = tab
    }

    private val _getTags = MutableStateFlow<TagsState>(TagsState.Loading)
    val getTags = _getTags.asStateFlow()

    private val _categoriesWithCourses = MutableStateFlow<PagingData<CategoryResponse.Category>>(PagingData.empty())
    val categoriesWithCourses: StateFlow<PagingData<CategoryResponse.Category>> = _categoriesWithCourses

    private val _historyMovie = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val historyMovie: StateFlow<PagingData<Movie>> = _historyMovie

    private val _getBanners = MutableStateFlow<PagingData<BannersResponse.Banner>>(
        PagingData.empty()
    )
    val getBanners: StateFlow<PagingData<BannersResponse.Banner>> = _getBanners


    private fun getTags() = screenModelScope.launch(Dispatchers.IO) {
        userRepository.getTags().collect { result ->
            result.onSuccess { movie ->
                movie?.let { item ->
                    _getTags.emit(TagsState.SuccessTags(item))
                }
            }
        }
    }

    fun fetchCategoriesWithCourses(isMovie:Boolean,slug:String? = null,showOnHomepage:Boolean? = null) {
        screenModelScope.launch {
            userRepository.getCategories(isMovie,slug,showOnHomepage)
                .cachedIn(screenModelScope)
                .catch { e ->

                }
                .onEach { pagingData ->
                    _categoriesWithCourses.tryEmit(pagingData)
                }.launchIn(screenModelScope)
        }
    }


    fun fetchHistoryMovie() {
        screenModelScope.launch {
            userRepository.getHistoryMovie()
                .cachedIn(screenModelScope)
                .catch { e -> }
                .onEach { pagingData ->
                    _historyMovie.tryEmit(pagingData)
                }.launchIn(screenModelScope)
        }
    }

    private fun getBanners() = screenModelScope.launch(Dispatchers.IO) {
        screenModelScope.launch {
            userRepository.getBanners()
                .cachedIn(screenModelScope)
                .catch { e ->

                }
                .onEach { pagingData ->
                    _getBanners.value = pagingData
                }.launchIn(screenModelScope)
        }
    }
}


sealed interface CategoriesState {
    data object Loading : CategoriesState
    data class Success(val movies: List<CategoryResponse.Category>?) : CategoriesState
    data class Error(val error: String) : CategoriesState
}

sealed interface MoviesState {
    data object Loading : MoviesState
    data class Success(val movies: List<Movie>?) : MoviesState
    data class Error(val error: String) : MoviesState
}

sealed interface TagsState {
    data object Loading : TagsState
    data class SuccessTags(val tags: TagsResponse?) : TagsState
    data class ErrorTags(val error: String) : TagsState
}

