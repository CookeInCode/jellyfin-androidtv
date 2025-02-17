package org.jellyfin.androidtvc.ui.search

import android.content.Context
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Row
import org.jellyfin.androidtvc.constant.QueryType
import org.jellyfin.androidtvc.data.service.BackgroundService
import org.jellyfin.androidtvc.ui.itemhandling.BaseRowItem
import org.jellyfin.androidtvc.ui.itemhandling.ItemLauncher
import org.jellyfin.androidtvc.ui.itemhandling.ItemRowAdapter
import org.jellyfin.androidtvc.ui.presentation.CardPresenter
import org.jellyfin.androidtvc.ui.presentation.CustomListRowPresenter
import org.jellyfin.androidtvc.ui.presentation.MutableObjectAdapter

class SearchFragmentDelegate(
	private val context: Context,
	private val backgroundService: BackgroundService,
) {
	val rowsAdapter = MutableObjectAdapter<Row>(CustomListRowPresenter())

	fun showResults(searchResultGroups: Collection<SearchResultGroup>) {
		rowsAdapter.clear()
		val adapters = mutableListOf<ItemRowAdapter>()
		for ((labelRes, baseItems) in searchResultGroups) {
			val adapter = ItemRowAdapter(
				context,
				baseItems.toList(),
				CardPresenter(),
				rowsAdapter,
				QueryType.Search
			).apply {
				setRow(ListRow(HeaderItem(context.getString(labelRes)), this))
			}
			adapters.add(adapter)
		}
		for (adapter in adapters) adapter.Retrieve()
	}

	val onItemViewClickedListener = OnItemViewClickedListener { _, item, _, row ->
		if (item !is BaseRowItem) return@OnItemViewClickedListener
		row as ListRow
		val adapter = row.adapter as ItemRowAdapter
		ItemLauncher.launch(item as BaseRowItem?, adapter, item.index, context)
	}

	val onItemViewSelectedListener = OnItemViewSelectedListener { _, item, _, _ ->
		val baseItem = item?.let { (item as BaseRowItem).baseItem }
		if (baseItem != null) {
			backgroundService.setBackground(baseItem)
		} else {
			backgroundService.clearBackgrounds()
		}
	}
}
