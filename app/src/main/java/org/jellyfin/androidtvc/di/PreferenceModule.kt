package org.jellyfin.androidtvc.di

import org.jellyfin.androidtvc.preference.LiveTvPreferences
import org.jellyfin.androidtvc.preference.PreferencesRepository
import org.jellyfin.androidtvc.preference.SystemPreferences
import org.jellyfin.androidtvc.preference.TelemetryPreferences
import org.jellyfin.androidtvc.preference.UserPreferences
import org.jellyfin.androidtvc.preference.UserSettingPreferences
import org.koin.dsl.module

val preferenceModule = module {
	single { PreferencesRepository(get(), get(), get()) }

	single { LiveTvPreferences(get()) }
	single { UserSettingPreferences(get()) }
	single { UserPreferences(get()) }
	single { SystemPreferences(get()) }
	single { TelemetryPreferences(get()) }
}
