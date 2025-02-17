package org.jellyfin.androidtvc.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.ui.SettingsPopup
import org.jellyfin.androidtvc.ui.playback.PlaybackController
import org.jellyfin.androidtvc.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter

class SettingAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue
) : CustomAction(context, customPlaybackTransportControlGlue) {
	var subtitlesPresent = false

	init {
		initializeWithIcon(R.drawable.ic_adjust)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		subtitlesPresent = videoPlayerAdapter.hasSubs()
		videoPlayerAdapter.leanbackOverlayFragment.setFading(false)
		return SettingsPopup(context, view,
			{ value -> playbackController.audioDelay = value },
			{ value -> playbackController.subtitleDelay = value }
		).apply {
			popupWindow.setOnDismissListener { videoPlayerAdapter.leanbackOverlayFragment.setFading(true) }
		}.show(
			subtitlesPresent,
			playbackController.audioDelay,
			playbackController.subtitleDelay
		)
	}
}
