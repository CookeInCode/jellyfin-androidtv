package org.jellyfin.androidtvc.ui

import androidx.annotation.DrawableRes

open class GridButton @JvmOverloads constructor(
	val id: Int,
	val text: String,
	@DrawableRes val imageRes: Int? = null,
) {
	override fun toString() = text
}
