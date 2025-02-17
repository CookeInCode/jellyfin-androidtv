package org.jellyfin.androidtvc.ui.playback.overlay.action

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.constant.getQualityProfiles
import org.jellyfin.androidtvc.preference.UserPreferences
import org.jellyfin.androidtvc.ui.playback.PlaybackController
import org.jellyfin.androidtvc.ui.playback.VideoQualityController
import org.jellyfin.androidtvc.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtvc.ui.playback.overlay.VideoPlayerAdapter

class SelectQualityAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
	userPreferences: UserPreferences
) : CustomAction(context, customPlaybackTransportControlGlue) {
	private val previousQualitySelection = userPreferences[UserPreferences.maxBitrate]
	private val qualityController = VideoQualityController(previousQualitySelection, userPreferences)
	private val qualityProfiles = getQualityProfiles(context)

	init {
		initializeWithIcon(R.drawable.ic_select_quality)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.leanbackOverlayFragment.setFading(false)
		PopupMenu(context, view, Gravity.END).apply {
			qualityProfiles.values.forEachIndexed { i, selected ->
				menu.add(0, i, i, selected)
			}

			menu.setGroupCheckable(0, true, true)
			menu.getItem(qualityProfiles.keys.indexOf(qualityController.currentQuality))?.let { item ->
				item.isChecked = true
			}

			setOnDismissListener { videoPlayerAdapter.leanbackOverlayFragment.setFading(true) }
			setOnMenuItemClickListener { menuItem ->
				qualityController.currentQuality = qualityProfiles.keys.elementAt(menuItem.itemId)
				playbackController.refreshStream()
				dismiss()
				true
			}
		}.show()
	}
}
