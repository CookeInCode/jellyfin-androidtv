package org.jellyfin.androidtvc.ui.playback.overlay.action

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.ui.playback.PlaybackController
import org.jellyfin.androidtvc.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter
import timber.log.Timber

class ClosedCaptionsAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	init {
		initializeWithIcon(R.drawable.ic_select_subtitle)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		if (playbackController.currentStreamInfo == null) {
			Timber.w("StreamInfo null trying to obtain subtitles")
			Toast.makeText(context, "Unable to obtain subtitle info", Toast.LENGTH_LONG).show()
			return
		}

		videoPlayerAdapter.leanbackOverlayFragment.setFading(false)
		PopupMenu(context, view, Gravity.END).apply {
			with(menu) {
				for (sub in playbackController.subtitleStreams) {
					add(0, sub.index, sub.index, sub.displayTitle).apply {
						isChecked = sub.index == playbackController.subtitleStreamIndex
					}
				}

				add(0, -1, 0, context.getString(R.string.lbl_none)).apply {
					isChecked = playbackController.subtitleStreamIndex == -1
				}

				setGroupCheckable(0, true, false)
			}
			setOnDismissListener { videoPlayerAdapter.leanbackOverlayFragment.setFading(true) }
			setOnMenuItemClickListener { item ->
				playbackController.switchSubtitleStream(item.itemId)
				true
			}
		}.show()
	}
}
