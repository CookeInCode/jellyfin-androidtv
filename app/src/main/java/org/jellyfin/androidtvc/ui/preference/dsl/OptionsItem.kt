package org.jellyfin.androidtvc.ui.preference.dsl

import androidx.preference.PreferenceCategory

interface OptionsItem {
	fun build(category: PreferenceCategory, container: OptionsUpdateFunContainer)
}
