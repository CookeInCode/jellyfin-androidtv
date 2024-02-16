package org.jellyfin.androidtvc.ui.playback

import org.jellyfin.androidtvc.preference.UserPreferences;

class VideoQualityController(
	previousQualitySelection: String,
	private val userPreferences: UserPreferences,
) {
	var currentQuality = previousQualitySelection
		set(value) {
			userPreferences[UserPreferences.maxBitrate] = value
			field = value
		}
}
