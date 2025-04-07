package chaintech.videoplayer.host

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.ScreenResize

class MediaPlayerHost(
    mediaUrl: String = "",
    isPaused: Boolean = false,
    isMuted: Boolean = false,
    initialSpeed: PlayerSpeed = PlayerSpeed.X1,
    initialVideoFitMode: ScreenResize = ScreenResize.FILL,
    isLooping: Boolean = true,
    startTimeInSeconds: Int? = null,
    isFullScreen: Boolean = false,
    isZoomEnabled: Boolean = true
) {
    // Internal states
    internal var url by mutableStateOf(mediaUrl)
    internal var speed by mutableStateOf(initialSpeed)
    internal var videoFitMode by mutableStateOf(initialVideoFitMode)
    internal var seekToTime: Int? by mutableStateOf(null)
    internal var isSliding by mutableStateOf(false)
    internal var isPaused by mutableStateOf(isPaused)
    internal var isMuted by mutableStateOf(isMuted)
    internal var isLooping by mutableStateOf(isLooping)
    internal var totalTime by mutableStateOf(0) // Total video duration
    internal var currentTime by mutableStateOf(0) // Current playback position
    internal var isBuffering by mutableStateOf(true)
    internal var playFromTime: Int? by mutableStateOf(startTimeInSeconds)
    internal var volumeLevel by mutableStateOf(if (isMuted) 0f else 1f) // Range 0.0 to 1.0
    internal var isFullScreen by mutableStateOf(isFullScreen)
    internal var isZoomEnabled by mutableStateOf(isZoomEnabled)

    var onEvent: ((MediaPlayerEvent) -> Unit)? = null
    var onError: ((MediaPlayerError) -> Unit)? = null

    // Public actions
    fun loadUrl(mediaUrl: String) {
        url = mediaUrl
    }

    fun play() {
        isPaused = false
        onEvent?.invoke(MediaPlayerEvent.PauseChange(isPaused))
    }

    fun pause() {
        isPaused = true
        onEvent?.invoke(MediaPlayerEvent.PauseChange(isPaused))
    }

    fun togglePlayPause() {
        isPaused = !isPaused
        onEvent?.invoke(MediaPlayerEvent.PauseChange(isPaused))
    }

    fun mute() {
        isMuted = true
        onEvent?.invoke(MediaPlayerEvent.MuteChange(isMuted))
    }

    fun unmute() {
        isMuted = false
        onEvent?.invoke(MediaPlayerEvent.MuteChange(isMuted))
    }

    fun toggleMuteUnmute() {
        isMuted = !isMuted
        onEvent?.invoke(MediaPlayerEvent.MuteChange(isMuted))
    }

    fun setSpeed(speed: PlayerSpeed) {
        this.speed = speed
    }

    fun seekTo(seconds: Int?) {
        isSliding = true
        seekToTime = seconds
        isSliding = false
    }

    fun setVideoFitMode(mode: ScreenResize) {
        videoFitMode = mode
    }

    fun setLooping(isLooping: Boolean) {
        this.isLooping = isLooping
    }

    fun toggleLoop() {
        this.isLooping = !this.isLooping
    }

    fun setVolume(level: Float) {
        volumeLevel = level.coerceIn(0f, 1f)
    }

    fun setFullScreen(isFullScreen: Boolean) {
        this.isFullScreen = isFullScreen
        onEvent?.invoke(MediaPlayerEvent.FullScreenChange(this.isFullScreen))
    }

    fun toggleFullScreen() {
        this.isFullScreen = !this.isFullScreen
        onEvent?.invoke(MediaPlayerEvent.FullScreenChange(this.isFullScreen))
    }

    internal fun setBufferingStatus(isBuffering: Boolean) {
        this.isBuffering = isBuffering
        onEvent?.invoke(MediaPlayerEvent.BufferChange(isBuffering))
    }

    // Internal-only setters for time values
    internal fun updateTotalTime(time: Int) {
        if (totalTime != time) {
            totalTime = time
            onEvent?.invoke(MediaPlayerEvent.TotalTimeChange(totalTime))
        }
    }

    internal fun updateCurrentTime(time: Int) {
        if (currentTime != time) {
            currentTime = time
            onEvent?.invoke(MediaPlayerEvent.CurrentTimeChange(currentTime))
        }
    }

    internal fun triggerMediaEnd() {
        onEvent?.invoke(MediaPlayerEvent.MediaEnd)
    }

    internal fun triggerError(error: MediaPlayerError) {
        onError?.invoke(error)
    }

}
