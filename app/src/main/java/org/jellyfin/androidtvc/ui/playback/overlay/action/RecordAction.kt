package org.jellyfin.androidtvc.ui.playback.overlay.action

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.ui.playback.PlaybackController
import org.jellyfin.androidtvc.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter

class RecordAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	companion object {
		const val INDEX_INACTIVE = 0
		const val INDEX_RECORDING = 1
	}

	init {
		val recordInactive = ContextCompat.getDrawable(context, R.drawable.ic_record)
		val recordActive = ContextCompat.getDrawable(context, R.drawable.ic_record_red)

		setDrawables(arrayOf(recordInactive, recordActive))
	}

	@Override
	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.toggleRecording()
	}
}
