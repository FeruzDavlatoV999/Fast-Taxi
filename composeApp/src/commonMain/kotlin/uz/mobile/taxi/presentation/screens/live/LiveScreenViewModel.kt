package uz.mobile.taxi.presentation.screens.live

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.remote.dto.Date
import uz.mobile.taxi.data.remote.dto.ProgramResponse
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.domain.model.Live

class LiveScreenViewModel(
    private val movieRepository: MovieRepository,
) : ScreenModel {

    var selectedDate: Date? by mutableStateOf(null)

    private val _live = MutableStateFlow<LiveState>(LiveState.Loading)
    val live = _live.asStateFlow()

    private val _dates = MutableStateFlow<LiveDateState>(LiveDateState.Loading)
    val dates = _dates.asStateFlow()

    private val _program =
        MutableStateFlow<PagingData<ProgramResponse.ScheduleProgram>>(PagingData.empty())
    val program: StateFlow<PagingData<ProgramResponse.ScheduleProgram>> = _program


    fun onTagSelected(date: Date?) {
        selectedDate = date
    }


    fun getUrl() = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getLiveUrl().collect { result ->
            result.onSuccess { url ->
                url?.let { item ->
                    _live.emit(LiveState.Success(item))
                }
            }.onLoading {
                _live.emit(LiveState.Loading)
            }
        }
    }

    fun getProgram(date: String) = screenModelScope.launch(Dispatchers.IO) {
        screenModelScope.launch {
            movieRepository.getProgram(date)
                .cachedIn(screenModelScope)
                .onEach { pagingData ->
                    _program.value = pagingData
                }.launchIn(screenModelScope)
        }
    }

    fun getDate() = screenModelScope.launch(Dispatchers.IO) {
        movieRepository.getLiveDates().collect { result ->
            result.onSuccess { url ->
                url?.let { item ->
                    val dates = item.dates.orEmpty()
                    dates.firstOrNull()?.let { selectFirstItem(it) }
                    _dates.emit(LiveDateState.Success(dates))
                }
            }.onLoading {
                _dates.emit(LiveDateState.Loading)
            }
        }
    }

    private fun selectFirstItem(first: Date) {
        selectedDate = first
        first.date?.let { getProgram(it) }
    }

}

sealed interface LiveState {
    data object Loading : LiveState
    data class Success(val movies: Live) : LiveState
    data class Error(val error: String) : LiveState
}

sealed interface LiveDateState {
    data object Loading : LiveDateState
    data class Success(val dates: List<Date>) : LiveDateState
    data class Error(val error: String) : LiveDateState
}