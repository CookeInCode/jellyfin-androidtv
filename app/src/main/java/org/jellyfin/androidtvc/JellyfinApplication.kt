package org.jellyfin.androidtvc

import android.app.Application
import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.vanniktech.blurhash.BlurHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.acra.ACRA
import org.jellyfin.androidtvc.data.eventhandling.SocketHandler
import org.jellyfin.androidtvc.data.repository.NotificationsRepository
import org.jellyfin.androidtvc.integration.LeanbackChannelWorker
import org.jellyfin.androidtvc.telemetry.TelemetryService
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

@Suppress("unused")
class JellyfinApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		// Don't run in ACRA service
		if (ACRA.isACRASenderServiceProcess()) return

		val notificationsRepository by inject<NotificationsRepository>()
		notificationsRepository.addDefaultNotifications()
	}

	/**
	 * Called from the StartupActivity when the user session is started.
	 */
	suspend fun onSessionStart() = withContext(Dispatchers.IO) {
		val workManager by inject<WorkManager>()
		val socketListener by inject<SocketHandler>()

		// Update background worker
		launch {
			// Cancel all current workers
			workManager.cancelAllWork().await()

			// Recreate periodic workers
			workManager.enqueueUniquePeriodicWork(
				LeanbackChannelWorker.PERIODIC_UPDATE_REQUEST_NAME,
				ExistingPeriodicWorkPolicy.UPDATE,
				PeriodicWorkRequestBuilder<LeanbackChannelWorker>(1, TimeUnit.HOURS)
					.setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
					.build()
			).await()
		}

		// Update WebSockets
		launch { socketListener.updateSession() }
	}

	override fun onLowMemory() {
		super.onLowMemory()

		BlurHash.clearCache()
	}

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)

		TelemetryService.init(this)
	}
}
