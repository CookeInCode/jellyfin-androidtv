package org.jellyfin.androidtvc.ui.playback.overlay.action

import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter

interface AndroidAction {
	fun onActionClicked(
		videoPlayerAdapter: VideoPlayerAdapter
	)
}
