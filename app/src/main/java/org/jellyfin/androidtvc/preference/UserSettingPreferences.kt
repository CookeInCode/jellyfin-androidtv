package org.jellyfin.androidtvc.preference

import org.jellyfin.androidtvc.constant.HomeSectionType
import org.jellyfin.androidtvc.preference.store.DisplayPreferencesStore
import org.jellyfin.preference.enumPreference
import org.jellyfin.preference.intPreference
import org.jellyfin.sdk.api.client.ApiClient

class UserSettingPreferences(
	api: ApiClient,
) : DisplayPreferencesStore(
	displayPreferencesId = "usersettings",
	api = api,
	app = "emby",
) {
	companion object {
		val skipBackLength = intPreference("skipBackLength", 10_000)
		val skipForwardLength = intPreference("skipForwardLength", 30_000)

		val homesection0 = enumPreference("homesection0", HomeSectionType.LIBRARY_TILES_SMALL)
		val homesection1 = enumPreference("homesection1", HomeSectionType.RESUME)
		val homesection2 = enumPreference("homesection2", HomeSectionType.LATEST_MEDIA)
		val homesection3 = enumPreference("homesection3", HomeSectionType.NONE)
		val homesection4 = enumPreference("homesection4", HomeSectionType.NONE)
		val homesection5 = enumPreference("homesection5", HomeSectionType.NONE)
		val homesection6 = enumPreference("homesection6", HomeSectionType.NONE)
	}

	val homesections
		get() = listOf(homesection0, homesection1, homesection2, homesection3, homesection4, homesection5, homesection6)
			.map(::get)
			.filterNot { it == HomeSectionType.NONE }
}
