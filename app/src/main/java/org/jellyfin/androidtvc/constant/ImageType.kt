package org.jellyfin.androidtvc.constant

import org.jellyfin.androidtvc.R
import org.jellyfin.preference.PreferenceEnum

enum class ImageType(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Poster.
	 */
	POSTER(R.string.image_type_poster),

	/**
	 * Thumbnail.
	 */
	THUMB(R.string.image_type_thumbnail),

	/**
	 * Banner.
	 */
	BANNER(R.string.image_type_banner),
}
