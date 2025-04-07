package chaintech.videoplayer.host

sealed class MediaPlayerEvent {
    data class MuteChange(val isMuted: Boolean) : MediaPlayerEvent()
    data class PauseChange(val isPaused: Boolean) : MediaPlayerEvent()
    data class BufferChange(val isBuffering: Boolean) : MediaPlayerEvent()
    data class CurrentTimeChange(val currentTime: Int) : MediaPlayerEvent()
    data class FullScreenChange(val isFullScreen: Boolean) : MediaPlayerEvent()
    data class TotalTimeChange(val totalTime: Int) : MediaPlayerEvent()
    data object MediaEnd : MediaPlayerEvent()
}


