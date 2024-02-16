package org.jellyfin.androidtvc.ui.preference.screen

import org.jellyfin.androidtvc.BuildConfig
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.preference.SystemPreferences
import org.jellyfin.androidtvc.preference.UserPreferences
import org.jellyfin.androidtvc.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtvc.ui.preference.dsl.checkbox
import org.jellyfin.androidtvc.ui.preference.dsl.optionsScreen
import org.jellyfin.androidtvc.util.isTvDevice
import org.koin.android.ext.android.inject

class DeveloperPreferencesScreen : OptionsFragment() {
	private val userPreferences: UserPreferences by inject()
	private val systemPreferences: SystemPreferences by inject()

	override val screen by optionsScreen {
		setTitle(R.string.pref_developer_link)

		category {
			// Legacy debug flag
			// Not in use by much components anymore
			checkbox {
				setTitle(R.string.lbl_enable_debug)
				setContent(R.string.desc_debug)
				bind(userPreferences, UserPreferences.debuggingEnabled)
			}

			// UI Mode toggle
			if (!context.isTvDevice()) {
				checkbox {
					setTitle(R.string.disable_ui_mode_warning)
					bind(systemPreferences, SystemPreferences.disableUiModeWarning)
				}
			}

			checkbox {
				setTitle(R.string.enable_reactive_homepage)
				setContent(R.string.enable_playback_module_description)
				bind(userPreferences, UserPreferences.homeReactive)
			}

			// Only show in debug mode
			// some strings are hardcoded because these options don't show in beta/release builds
			if (BuildConfig.DEVELOPMENT) {
				checkbox {
					title = "Enable new playback module for video"
					setContent(R.string.enable_playback_module_description)

					bind(userPreferences, UserPreferences.playbackRewriteVideoEnabled)
				}
			}
		}
	}
}
