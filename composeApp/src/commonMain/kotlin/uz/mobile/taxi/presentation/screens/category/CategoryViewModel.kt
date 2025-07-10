package uz.mobile.taxi.presentation.screens.category

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.dto.MovieDetailResponse
import uz.mobile.taxi.data.repository.MovieRepository

class CategoryViewModel(
    private val userRepository: MovieRepository,
) : ScreenModel {

    private val _categoriesWithCourses = MutableStateFlow<PagingData<MovieDetailResponse>>(PagingData.empty())
    val categoriesWithCourses: StateFlow<PagingData<MovieDetailResponse>> = _categoriesWithCourses

    fun fetchCategoriesWithCourses(id:Int) {
        screenModelScope.launch {
            userRepository.getMovies(id)
                .cachedIn(screenModelScope)
                .catch { e ->
                    Exception(e)
                }
                .onEach { pagingData ->
                    _categoriesWithCourses.value = pagingData
                }.launchIn(screenModelScope)
        }
    }

}