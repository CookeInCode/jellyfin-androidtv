package org.jellyfin.androidtvc.ui.itemdetail

import androidx.core.view.isVisible
import androidx.leanback.widget.Row
import org.jellyfin.androidtvc.data.model.InfoItem
import org.jellyfin.androidtvc.ui.TextUnderButton
import org.jellyfin.sdk.model.api.BaseItemDto

class MyDetailsOverviewRow @JvmOverloads constructor(
	val item: BaseItemDto,
	var imageDrawable: String? = null,
	var summary: String? = null,
	var progress: Int = 0,
	var infoItem1: InfoItem? = null,
	var infoItem2: InfoItem? = null,
	var infoItem3: InfoItem? = null,
	var selectedMediaSourceIndex: Int = 0,
) : Row() {
	private val _actions = mutableListOf<TextUnderButton>()
	val actions get() = _actions.toList()
	val visibleActions get() = _actions.count { it.isVisible }

	fun addAction(button: TextUnderButton) = _actions.add(button)
}
