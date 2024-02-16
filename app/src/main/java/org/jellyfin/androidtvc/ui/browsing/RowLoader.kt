package org.jellyfin.androidtvc.ui.browsing

interface RowLoader {
	fun loadRows(rows: MutableList<BrowseRowDef>)
}
