package uz.mobile.taxi.presentation.screens.play

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.domain.model.Movie
import uz.mobile.taxi.domain.model.Season
import uz.mobile.taxi.domain.model.Type
import uz.mobile.taxi.domain.model.WatchInfo

class PlayScreenViewModel(
    private val movie: Movie,
    private val movieRepository: MovieRepository
) : ScreenModel {

    var currentTime by mutableStateOf(0)
    private var watchableId by mutableStateOf(0)
    private var watchableType by mutableStateOf(movie.type)


    var selectedSeason: Season? by mutableStateOf(null)
        private set

    var selectedEpisode: Movie by mutableStateOf(Movie())
        private set

    var seasons: List<Season> by mutableStateOf(emptyList())
        private set

    var episodes: List<Movie> by mutableStateOf(emptyList())
        private set


    init {
        selectEpisode(movie)
        setSeason(movie.seasons)
        startSendingRequests()
    }

    private var isRequestLoopRunning = false

    fun updateWatchableInfo(id: Int, type: Type, time: Int) {
        watchableId = id
        watchableType = type
        currentTime = time
    }

    private fun startSendingRequests() {
        if (!isRequestLoopRunning) {
            isRequestLoopRunning = true
            screenModelScope.launch {
                while (true) {
                    sendRequest(WatchInfo(watchableId, watchableType, currentTime))
                    delay(15000)
                }
            }

        }
    }

    private suspend fun sendRequest(watchInfo: WatchInfo) {
        movieRepository.setWatchInfo(watchInfo).collect { result ->
            result.onSuccess {

            }
        }
    }

    fun setSeason(list: List<Season>) {
        if (selectedSeason == null && list.isNotEmpty()) {
            selectSeason(list.first())
        }
        seasons = list
    }


    fun selectSeason(data: Season) {
        selectedSeason = data
        episodes = data.episodes
    }

    fun selectEpisode(chapter: Movie) {
        selectedEpisode = chapter
        watchableId = chapter.id
        watchableType = chapter.type
    }


}