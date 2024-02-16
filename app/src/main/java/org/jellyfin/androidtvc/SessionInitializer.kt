package org.jellyfin.androidtvc

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import kotlinx.coroutines.launch
import org.jellyfin.androidtvc.auth.repository.SessionRepository
import org.jellyfin.androidtvc.di.KoinInitializer

@Suppress("unused")
class SessionInitializer : Initializer<Unit> {
	override fun create(context: Context) {
		val koin = AppInitializer.getInstance(context)
			.initializeComponent(KoinInitializer::class.java)
			.koin

		// Restore system session
		ProcessLifecycleOwner.get().lifecycleScope.launch {
			koin.get<SessionRepository>().restoreSession(destroyOnly = false)
		}
	}

	override fun dependencies() = listOf(KoinInitializer::class.java)
}
