package chaintech.videoplayer.host

sealed class MediaPlayerError(val message: String) {
    object VlcNotFound : MediaPlayerError("VLC library not found.")
    data class InitializationError(val details: String) : MediaPlayerError(details)
    data class PlaybackError(val details: String) : MediaPlayerError(details)
    data class ResourceError(val details: String) : MediaPlayerError(details)
}