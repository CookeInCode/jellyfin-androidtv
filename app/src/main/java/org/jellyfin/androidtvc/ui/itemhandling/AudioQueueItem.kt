package org.jellyfin.androidtvc.ui.itemhandling

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind

class AudioQueueItem(
	index: Int,
	item: BaseItemDto,
) : BaseRowItem(
	baseRowType = when (item.type) {
		BaseItemKind.PROGRAM -> BaseRowType.LiveTvProgram
		BaseItemKind.RECORDING -> BaseRowType.LiveTvRecording
		else -> BaseRowType.BaseItem
	},
	index = index,
	staticHeight = true,
	baseItem = item,
)
