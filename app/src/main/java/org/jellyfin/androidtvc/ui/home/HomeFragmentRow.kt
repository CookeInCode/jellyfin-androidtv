package org.jellyfin.androidtvc.ui.home

import android.content.Context
import androidx.leanback.widget.Row
import org.jellyfin.androidtvc.ui.presentation.CardPresenter
import org.jellyfin.androidtvc.ui.presentation.MutableObjectAdapter

interface HomeFragmentRow {
	fun addToRowsAdapter(context: Context, cardPresenter: CardPresenter, rowsAdapter: MutableObjectAdapter<Row>)
}
