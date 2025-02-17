package org.jellyfin.androidtvc.preference.constant

import org.jellyfin.androidtvc.R
import org.jellyfin.preference.PreferenceEnum

enum class PreferredVideoPlayer(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 *  Automatically selects between exoplayer and vlc
	 */
	AUTO(R.string.pref_video_player_auto),

	/**
	 *  Force ExoPlayer
	 */
	EXOPLAYER(R.string.pref_video_player_exoplayer),

	/**
	 * Force libVLC
	 */
	VLC(R.string.pref_video_player_vlc),

	/**
	 * Use external player
	 */
	EXTERNAL(R.string.pref_video_player_external),

	/**
	 * Choose a player - play with button
	 */
	CHOOSE(R.string.pref_video_player_choose),
}
