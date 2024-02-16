package org.jellyfin.androidtvc.ui.home

import android.content.Context
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Row
import org.jellyfin.androidtvc.constant.QueryType
import org.jellyfin.androidtvc.data.querying.ViewQuery
import org.jellyfin.androidtvc.preference.UserPreferences
import org.jellyfin.androidtvc.ui.browsing.BrowseRowDef
import org.jellyfin.androidtvc.ui.itemhandling.ItemRowAdapter
import org.jellyfin.androidtvc.ui.presentation.CardPresenter
import org.jellyfin.androidtvc.ui.presentation.MutableObjectAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeFragmentBrowseRowDefRow(
	private val browseRowDef: BrowseRowDef
) : HomeFragmentRow, KoinComponent {
	private val userPreferences by inject<UserPreferences>()

	override fun addToRowsAdapter(context: Context, cardPresenter: CardPresenter, rowsAdapter: MutableObjectAdapter<Row>) {
		val header = HeaderItem(browseRowDef.headerText)
		val preferParentThumb = userPreferences[UserPreferences.seriesThumbnailsEnabled]

		// Some of these members are probably never used and could be removed
		val rowAdapter = when (browseRowDef.queryType) {
			QueryType.NextUp -> ItemRowAdapter(context, browseRowDef.nextUpQuery, preferParentThumb, cardPresenter, rowsAdapter)
			QueryType.LatestItems -> ItemRowAdapter(context, browseRowDef.latestItemsQuery, userPreferences[UserPreferences.seriesThumbnailsEnabled], cardPresenter, rowsAdapter)
			QueryType.Season -> ItemRowAdapter(context, browseRowDef.seasonQuery, cardPresenter, rowsAdapter)
			QueryType.Upcoming -> ItemRowAdapter(context, browseRowDef.upcomingQuery, cardPresenter, rowsAdapter)
			QueryType.Views -> ItemRowAdapter(context, ViewQuery, cardPresenter, rowsAdapter)
			QueryType.SimilarSeries -> ItemRowAdapter(context, browseRowDef.similarQuery, QueryType.SimilarSeries, cardPresenter, rowsAdapter)
			QueryType.SimilarMovies -> ItemRowAdapter(context, browseRowDef.similarQuery, QueryType.SimilarMovies, cardPresenter, rowsAdapter)
			QueryType.Persons -> ItemRowAdapter(context, browseRowDef.personsQuery, browseRowDef.chunkSize, cardPresenter, rowsAdapter)
			QueryType.LiveTvChannel -> ItemRowAdapter(context, browseRowDef.tvChannelQuery, 40, cardPresenter, rowsAdapter)
			QueryType.LiveTvProgram -> ItemRowAdapter(context, browseRowDef.programQuery, cardPresenter, rowsAdapter)
			QueryType.LiveTvRecording -> ItemRowAdapter(context, browseRowDef.recordingQuery, browseRowDef.chunkSize, cardPresenter, rowsAdapter)
			QueryType.Resume -> ItemRowAdapter(context, browseRowDef.resumeQuery, browseRowDef.chunkSize, browseRowDef.preferParentThumb, browseRowDef.isStaticHeight, cardPresenter, rowsAdapter)
			else -> ItemRowAdapter(context, browseRowDef.query, browseRowDef.chunkSize, browseRowDef.preferParentThumb, browseRowDef.isStaticHeight, cardPresenter, rowsAdapter, browseRowDef.queryType)
		}

		rowAdapter.setReRetrieveTriggers(browseRowDef.changeTriggers)
		val row = ListRow(header, rowAdapter)
		rowAdapter.setRow(row)
		rowAdapter.Retrieve()
		rowsAdapter.add(row)
	}
}
