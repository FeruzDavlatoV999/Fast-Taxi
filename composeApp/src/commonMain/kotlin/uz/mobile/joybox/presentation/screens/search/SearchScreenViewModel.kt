package uz.mobile.joybox.presentation.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.mobile.joybox.data.repository.SearchRepository
import uz.mobile.joybox.domain.model.Movie
import uz.mobile.joybox.domain.model.Tag
import uz.mobile.joybox.domain.util.UniversalText

class SearchScreenViewModel(
    private val repository: SearchRepository
) : ScreenModel {


    var selectedTagId: Int? by mutableStateOf(null)

    var tags: List<Tag> by mutableStateOf(emptyList())
        private set

    var searchText: String by mutableStateOf("")
        private set

    var state: MoviesState by mutableStateOf(MoviesState.Idl)
        private set

    private var searchJob: Job? = null
    private val debounceDelay = 500L


    fun onSearchTextChange(text: String) {
        searchText = text
        debounceSearch()
    }

    fun onTagSelected(id: Int?) {
        selectedTagId = id
        debounceSearch()
    }

    fun getTags() = screenModelScope.launch(Dispatchers.IO) {
        repository.getTags().collect { result ->
            result.onSuccess { list ->
                tags = list
            }.onLoading {

            }.onMessage {

            }
        }
    }

    private fun debounceSearch() {
        searchJob?.cancel()
        searchJob = screenModelScope.launch {
            delay(debounceDelay)
            search()
        }
    }


    private fun search() = screenModelScope.launch(Dispatchers.IO) {
        repository.searchMovie(searchText, selectedTagId, 1).collect { result ->
            result.onSuccess { list ->
                state = if (list.isEmpty()) MoviesState.Empty else MoviesState.Success(list)
            }.onMessage {
                state = MoviesState.Error(it)
            }.onLoading {
                state = MoviesState.Loading
            }
        }
    }

}

sealed class MoviesState {
    data class Success(val movies: List<Movie>) : MoviesState()
    data object Empty : MoviesState()
    data object Loading : MoviesState()
    data class Error(val message: UniversalText) : MoviesState()
    data object Idl : MoviesState()
}