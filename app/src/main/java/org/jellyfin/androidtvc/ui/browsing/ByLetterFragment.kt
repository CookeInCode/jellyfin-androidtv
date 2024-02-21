package org.jellyfin.androidtvc.ui.browsing

import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.data.querying.StdItemQuery
import org.jellyfin.sdk.model.constant.ItemSortBy

class ByLetterFragment : BrowseFolderFragment() {

	override fun onStart() {
		super.onStart()

		// Hide the title provided by the Leanback library
		title = null
	}

	override suspend fun setupQueries(rowLoader: RowLoader) {
		val childCount = folder?.childCount ?: 0
		if (childCount <= 0) return

		val letters = getString(R.string.byletter_letters)

		// Add a '#' item
		val numbersQuery = StdItemQuery().apply {
			parentId = folder?.id?.toString()
			sortBy = arrayOf(ItemSortBy.SortName)
			includeType?.let { includeItemTypes = arrayOf(it) }
			nameLessThan = letters.substring(0, 1)
			recursive = true
		}

		rows.add(BrowseRowDef("#", numbersQuery, 40))

		// Add all the defined letters
		for (letter in letters.toCharArray()) {
			val letterQuery = StdItemQuery().apply {
				parentId = folder?.id?.toString()
				sortBy = arrayOf(ItemSortBy.SortName)
				includeType?.let { includeItemTypes = arrayOf(it) }
				nameStartsWith = letter.toString()
				recursive = true
			}

			rows.add(BrowseRowDef(letter.toString(), letterQuery, 40))
		}

		rowLoader.loadRows(rows)
	}
}
