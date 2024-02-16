package org.jellyfin.androidtvc.constant

sealed interface CustomMessage {
	data object RefreshCurrentItem : CustomMessage
	data object ActionComplete : CustomMessage
}
