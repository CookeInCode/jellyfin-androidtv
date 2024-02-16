package org.jellyfin.androidtvc.di

import org.jellyfin.androidtvc.auth.AccountManagerMigration
import org.jellyfin.androidtvc.auth.apiclient.ApiBinder
import org.jellyfin.androidtvc.auth.repository.AuthenticationRepository
import org.jellyfin.androidtvc.auth.repository.AuthenticationRepositoryImpl
import org.jellyfin.androidtvc.auth.repository.ServerRepository
import org.jellyfin.androidtvc.auth.repository.ServerRepositoryImpl
import org.jellyfin.androidtvc.auth.repository.ServerUserRepository
import org.jellyfin.androidtvc.auth.repository.ServerUserRepositoryImpl
import org.jellyfin.androidtvc.auth.repository.SessionRepository
import org.jellyfin.androidtvc.auth.repository.SessionRepositoryImpl
import org.jellyfin.androidtvc.auth.store.AuthenticationPreferences
import org.jellyfin.androidtvc.auth.store.AuthenticationStore
import org.koin.dsl.module

val authModule = module {
	single { AccountManagerMigration(get()) }
	single { AuthenticationStore(get(), get()) }
	single { AuthenticationPreferences(get()) }

	single<AuthenticationRepository> {
		AuthenticationRepositoryImpl(get(), get(), get(), get(), get(), get(defaultDeviceInfo))
	}
	single<ServerRepository> { ServerRepositoryImpl(get(), get()) }
	single<ServerUserRepository> { ServerUserRepositoryImpl(get(), get()) }
	single<SessionRepository> {
		SessionRepositoryImpl(get(), get(), get(), get(), get(), get(defaultDeviceInfo), get(), get(), get())
	}

	single { ApiBinder(get(), get()) }
}
