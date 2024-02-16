package org.jellyfin.androidtvc.ui.playback

interface PlaybackControllerNotifiable {
	fun onCompletion()
	fun onError()
	fun onPrepared()
	fun onProgress()
	fun onPlaybackSpeedChange(newSpeed: Float)
}
