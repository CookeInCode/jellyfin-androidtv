package org.jellyfin.androidtvc.ui.livetv

import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.preference.SystemPreferences
import org.jellyfin.androidtvc.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtvc.ui.preference.dsl.OptionsItemCheckbox
import org.jellyfin.androidtvc.ui.preference.dsl.checkbox
import org.jellyfin.androidtvc.ui.preference.dsl.optionsScreen
import org.koin.android.ext.android.inject

class GuideFiltersScreen : OptionsFragment() {
	private val systemPreferences: SystemPreferences by inject()

	override val screen by optionsScreen {
		setTitle(R.string.lbl_filters)

		category {
			setOf(
				SystemPreferences.liveTvGuideFilterMovies to getString(R.string.lbl_movies),
				SystemPreferences.liveTvGuideFilterSeries to getString(R.string.lbl_series),
				SystemPreferences.liveTvGuideFilterNews to getString(R.string.lbl_news),
				SystemPreferences.liveTvGuideFilterKids to getString(R.string.lbl_kids),
				SystemPreferences.liveTvGuideFilterSports to getString(R.string.lbl_sports),
				SystemPreferences.liveTvGuideFilterPremiere to getString(R.string.lbl_new_only),
			).forEach { (preference, label) ->
				checkbox {
					title = label
					type = OptionsItemCheckbox.Type.SWITCH
					bind(systemPreferences, preference)
				}
			}
		}
	}
}
