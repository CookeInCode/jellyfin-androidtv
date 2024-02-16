package org.jellyfin.androidtvc.ui.playback

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.jellyfin.androidtvc.data.repository.ItemMutationRepository
import org.koin.android.ext.android.inject
import java.util.UUID

fun ExternalPlayerActivity.markPlayed(item: UUID) {
	val itemMutationRepository by inject<ItemMutationRepository>()

	lifecycleScope.launch {
		itemMutationRepository.setPlayed(item, true)
	}
}
