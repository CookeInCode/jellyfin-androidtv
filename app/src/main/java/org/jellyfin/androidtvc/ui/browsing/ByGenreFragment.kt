package org.jellyfin.androidtvc.ui.browsing

import org.jellyfin.androidtvc.data.querying.StdItemQuery
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.genresApi
import org.jellyfin.sdk.model.constant.ItemSortBy
import org.koin.android.ext.android.inject

class ByGenreFragment : BrowseFolderFragment() {
	private val apiClient by inject<ApiClient>()

	override fun onStart() {
		super.onStart()

		// Hide the title provided by the Leanback library
		title = null
	}

	override suspend fun setupQueries(rowLoader: RowLoader) {
		val childCount = folder?.childCount ?: 0
		if (childCount <= 0) return

		// Get all genres for this folder
		val genresResponse by apiClient.genresApi.getGenres(
			parentId = folder?.id,
			sortBy = setOf(ItemSortBy.SortName),
			userId = apiClient.userId,
		)

		for (genre in genresResponse.items.orEmpty()) {
			val itemsQuery = StdItemQuery().apply {
				parentId = folder?.id.toString()
				sortBy = arrayOf(ItemSortBy.SortName)
				includeType?.let { includeItemTypes = arrayOf(it) }
				genres = arrayOf(genre.name)
				recursive = true
			}
			rows.add(BrowseRowDef(genre.name, itemsQuery, 40))
		}

		rowLoader.loadRows(rows)
	}
}
