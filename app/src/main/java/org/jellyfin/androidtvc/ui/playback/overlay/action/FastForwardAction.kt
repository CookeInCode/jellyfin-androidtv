package org.jellyfin.androidtvc.ui.playback.overlay.action

import android.content.Context
import androidx.leanback.widget.PlaybackControlsRow
import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter

class FastForwardAction(context: Context) : PlaybackControlsRow.FastForwardAction(context),
	AndroidAction {
	override fun onActionClicked(videoPlayerAdapter: VideoPlayerAdapter) =
		videoPlayerAdapter.fastForward()
}

