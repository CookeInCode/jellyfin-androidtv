package org.jellyfin.androidtvc.preference

import org.jellyfin.androidtvc.constant.GridDirection
import org.jellyfin.androidtvc.constant.ImageType
import org.jellyfin.androidtvc.constant.PosterSize
import org.jellyfin.androidtvc.preference.store.DisplayPreferencesStore
import org.jellyfin.preference.booleanPreference
import org.jellyfin.preference.enumPreference
import org.jellyfin.preference.stringPreference
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.SortOrder
import org.jellyfin.sdk.model.constant.ItemSortBy

class LibraryPreferences(
	displayPreferencesId: String,
	api: ApiClient,
) : DisplayPreferencesStore(
	displayPreferencesId = displayPreferencesId,
	api = api,
) {
	companion object {
		val posterSize = enumPreference("PosterSize", PosterSize.LARGE)
		val imageType = enumPreference("ImageType", ImageType.POSTER)
		val gridDirection = enumPreference("GridDirection", GridDirection.VERTICAL)
		val enableSmartScreen = booleanPreference("SmartScreen", true)

		// Filters
		val filterFavoritesOnly = booleanPreference("FilterFavoritesOnly", false)
		val filterUnwatchedOnly = booleanPreference("FilterUnwatchedOnly", false)

		// Item sorting
		val sortBy = stringPreference("SortBy", ItemSortBy.SortName)
		val sortOrder = enumPreference("SortOrder", SortOrder.ASCENDING)
	}
}
