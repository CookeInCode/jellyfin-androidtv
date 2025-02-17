package org.jellyfin.androidtvc.data.repository

import org.jellyfin.androidtvc.data.model.DataRefreshService
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.playStateApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.UserItemDataDto

interface ItemMutationRepository {
	suspend fun setFavorite(item: UUID, favorite: Boolean): UserItemDataDto
	suspend fun setPlayed(item: UUID, played: Boolean): UserItemDataDto
}

class ItemMutationRepositoryImpl(
	private val api: ApiClient,
	private val dataRefreshService: DataRefreshService,
) : ItemMutationRepository {
	override suspend fun setFavorite(item: UUID, favorite: Boolean): UserItemDataDto {
		val response by when {
			favorite -> api.userLibraryApi.markFavoriteItem(itemId = item)
			else -> api.userLibraryApi.unmarkFavoriteItem(itemId = item)
		}

		dataRefreshService.lastFavoriteUpdate = System.currentTimeMillis()
		return response
	}

	override suspend fun setPlayed(item: UUID, played: Boolean): UserItemDataDto {
		val response by when {
			played -> api.playStateApi.markPlayedItem(itemId = item)
			else -> api.playStateApi.markUnplayedItem(itemId = item)
		}

		return response
	}
}
