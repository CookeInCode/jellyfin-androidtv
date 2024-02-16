package org.jellyfin.androidtvc.constant

import org.jellyfin.androidtvc.R
import org.jellyfin.preference.PreferenceEnum

enum class GridDirection(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Horizontal.
	 */
	HORIZONTAL(R.string.grid_direction_horizontal),

	/**
	 * Vertical.
	 */
	VERTICAL(R.string.grid_direction_vertical),
}
