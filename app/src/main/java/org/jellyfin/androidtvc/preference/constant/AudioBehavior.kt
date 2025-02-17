package org.jellyfin.androidtvc.preference.constant

import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.util.DeviceUtils
import org.jellyfin.preference.PreferenceEnum

enum class AudioBehavior(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Directly stream audio without any changes
	 */
	DIRECT_STREAM(R.string.pref_audio_direct),

	/**
	 * Downnmix audio to stereo. Disables the AC3, EAC3 and AAC_LATM audio codecs.
	 */
	DOWNMIX_TO_STEREO(R.string.pref_audio_compat),
}

val defaultAudioBehavior = if (DeviceUtils.isChromecastWithGoogleTV) AudioBehavior.DOWNMIX_TO_STEREO
else AudioBehavior.DIRECT_STREAM
