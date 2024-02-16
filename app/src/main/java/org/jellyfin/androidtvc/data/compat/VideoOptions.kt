package org.jellyfin.androidtvc.data.compat

class VideoOptions : AudioOptions() {
	var audioStreamIndex: Int? = null
	var subtitleStreamIndex: Int? = null
}
