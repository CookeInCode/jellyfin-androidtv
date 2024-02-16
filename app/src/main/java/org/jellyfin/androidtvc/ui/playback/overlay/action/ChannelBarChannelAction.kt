package org.jellyfin.androidtvc.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.ui.playback.PlaybackController
import org.jellyfin.androidtvc.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter

class ChannelBarChannelAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	init {
		initializeWithIcon(R.drawable.ic_channel_bar)
	}

	@Override
	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.leanbackOverlayFragment.hideOverlay()
		videoPlayerAdapter.masterOverlayFragment.showQuickChannelChanger();
	}
}
