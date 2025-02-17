package org.jellyfin.androidtvc.ui.preference.screen

import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.constant.HomeSectionType
import org.jellyfin.androidtvc.preference.UserSettingPreferences
import org.jellyfin.androidtvc.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtvc.ui.preference.dsl.enum
import org.jellyfin.androidtvc.ui.preference.dsl.optionsScreen
import org.jellyfin.preference.store.PreferenceStore
import org.koin.android.ext.android.inject

class HomePreferencesScreen : OptionsFragment() {
	private val userSettingPreferences: UserSettingPreferences by inject()

	override val stores: Array<PreferenceStore<*, *>>
		get() = arrayOf(userSettingPreferences)

	override val screen by optionsScreen {
		setTitle(R.string.home_prefs)

		category {
			setTitle(R.string.home_sections)

			arrayOf(
				UserSettingPreferences.homesection0,
				UserSettingPreferences.homesection1,
				UserSettingPreferences.homesection2,
				UserSettingPreferences.homesection3,
				UserSettingPreferences.homesection4,
				UserSettingPreferences.homesection5,
				UserSettingPreferences.homesection6,
			).forEachIndexed { index, section ->
				enum<HomeSectionType> {
					title = getString(R.string.home_section_i, index + 1)
					bind(userSettingPreferences, section)
				}
			}
		}
	}
}
