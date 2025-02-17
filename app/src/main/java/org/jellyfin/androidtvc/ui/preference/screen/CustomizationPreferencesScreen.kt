package org.jellyfin.androidtvc.ui.preference.screen

import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.preference.UserPreferences
import org.jellyfin.androidtvc.preference.constant.AppTheme
import org.jellyfin.androidtvc.preference.constant.ClockBehavior
import org.jellyfin.androidtvc.preference.constant.RatingType
import org.jellyfin.androidtvc.preference.constant.WatchedIndicatorBehavior
import org.jellyfin.androidtvc.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtvc.ui.preference.dsl.checkbox
import org.jellyfin.androidtvc.ui.preference.dsl.enum
import org.jellyfin.androidtvc.ui.preference.dsl.link
import org.jellyfin.androidtvc.ui.preference.dsl.list
import org.jellyfin.androidtvc.ui.preference.dsl.optionsScreen
import org.jellyfin.androidtvc.ui.preference.dsl.shortcut
import org.jellyfin.androidtvc.util.getQuantityString
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class CustomizationPreferencesScreen : OptionsFragment() {
	private val userPreferences: UserPreferences by inject()

	override val screen by optionsScreen {
		setTitle(R.string.pref_customization)

		category {
			setTitle(R.string.pref_theme)

			enum<AppTheme> {
				setTitle(R.string.pref_app_theme)
				bind(userPreferences, UserPreferences.appTheme)
			}


			enum<WatchedIndicatorBehavior> {
				setTitle(R.string.pref_watched_indicator)
				bind(userPreferences, UserPreferences.watchedIndicatorBehavior)
			}

			checkbox {
				setTitle(R.string.lbl_show_backdrop)
				setContent(R.string.pref_show_backdrop_description)
				bind(userPreferences, UserPreferences.backdropEnabled)
			}

			checkbox {
				setTitle(R.string.lbl_use_series_thumbnails)
				setContent(R.string.lbl_use_series_thumbnails_description)
				bind(userPreferences, UserPreferences.seriesThumbnailsEnabled)
			}

			enum<RatingType> {
				setTitle(R.string.pref_default_rating)
				bind(userPreferences, UserPreferences.defaultRatingType)
			}

			checkbox {
				setTitle(R.string.lbl_show_premieres)
				setContent(R.string.desc_premieres)
				bind(userPreferences, UserPreferences.premieresEnabled)
			}

			checkbox {
				setTitle(R.string.pref_enable_media_management)
				setContent(R.string.pref_enable_media_management_description)
				bind(userPreferences, UserPreferences.mediaManagementEnabled)
			}
		}

		category {
			setTitle(R.string.pref_browsing)

			link {
				setTitle(R.string.home_prefs)
				setContent(R.string.pref_home_description)
				icon = R.drawable.ic_house
				withFragment<HomePreferencesScreen>()
			}

			link {
				setTitle(R.string.pref_libraries)
				setContent(R.string.pref_libraries_description)
				icon = R.drawable.ic_grid
				withFragment<LibrariesPreferencesScreen>()
			}
		}

		category {
			setTitle(R.string.pref_screensaver)

			checkbox {
				setTitle(R.string.pref_screensaver_inapp_enabled)
				setContent(R.string.pref_screensaver_inapp_enabled_description)
				bind(userPreferences, UserPreferences.screensaverInAppEnabled)
			}

			@Suppress("MagicNumber")
			list {
				setTitle(R.string.pref_screensaver_inapp_timeout)

				entries = mapOf(
					30.seconds to context.getQuantityString(R.plurals.seconds, 30),
					1.minutes to context.getQuantityString(R.plurals.minutes, 1),
					2.5.minutes to context.getQuantityString(R.plurals.minutes, 2.5),
					5.minutes to context.getQuantityString(R.plurals.minutes, 5),
					10.minutes to context.getQuantityString(R.plurals.minutes, 10),
					15.minutes to context.getQuantityString(R.plurals.minutes, 15),
					30.minutes to context.getQuantityString(R.plurals.minutes, 30),
				).mapKeys { it.key.inWholeMilliseconds.toString() }

				bind {
					get { userPreferences[UserPreferences.screensaverInAppTimeout].toString() }
					set { value -> userPreferences[UserPreferences.screensaverInAppTimeout] = value.toLong() }
					default { UserPreferences.screensaverInAppTimeout.defaultValue.toString() }
				}

				depends { userPreferences[UserPreferences.screensaverInAppEnabled] }
			}
		}

		category {
			setTitle(R.string.pref_behavior)

			shortcut {
				setTitle(R.string.pref_audio_track_button)
				bind(userPreferences, UserPreferences.shortcutAudioTrack)
			}

			shortcut {
				setTitle(R.string.pref_subtitle_track_button)
				bind(userPreferences, UserPreferences.shortcutSubtitleTrack)
			}
		}
	}
}
