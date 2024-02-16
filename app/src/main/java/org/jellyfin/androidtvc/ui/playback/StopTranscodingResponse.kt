package org.jellyfin.androidtvc.ui.playback

import org.jellyfin.androidtvc.data.compat.StreamInfo
import org.jellyfin.androidtvc.data.compat.VideoOptions
import org.jellyfin.apiclient.interaction.ApiClient
import org.jellyfin.apiclient.interaction.EmptyResponse
import org.jellyfin.apiclient.interaction.Response
import org.jellyfin.sdk.model.DeviceInfo
import timber.log.Timber

class StopTranscodingResponse(
	private val playbackManager: PlaybackManager,
	private val deviceInfo: DeviceInfo,
	private val options: VideoOptions,
	private val startPositionTicks: Long,
	private val apiClient: ApiClient,
	private val response: Response<StreamInfo>,
) : EmptyResponse() {
	override fun onResponse() {
		playbackManager.getVideoStreamInfo(deviceInfo, options, startPositionTicks, apiClient, response)
	}

	override fun onError(ex: Exception) {
		Timber.e(ex, "Error in StopTranscodingProcesses")
		onResponse()
	}
}
