package org.jellyfin.androidtvc.data.compat

import org.jellyfin.apiclient.model.dlna.PlaybackErrorCode

class PlaybackException : RuntimeException() {
	var errorCode = PlaybackErrorCode.NotAllowed
}
