package org.jellyfin.androidtvc.di

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import org.jellyfin.androidtvc.BuildConfig
import org.jellyfin.androidtvc.auth.repository.ServerRepository
import org.jellyfin.androidtvc.auth.repository.UserRepository
import org.jellyfin.androidtvc.auth.repository.UserRepositoryImpl
import org.jellyfin.androidtvc.data.eventhandling.SocketHandler
import org.jellyfin.androidtvc.data.model.DataRefreshService
import org.jellyfin.androidtvc.data.repository.CustomMessageRepository
import org.jellyfin.androidtvc.data.repository.CustomMessageRepositoryImpl
import org.jellyfin.androidtvc.data.repository.ItemMutationRepository
import org.jellyfin.androidtvc.data.repository.ItemMutationRepositoryImpl
import org.jellyfin.androidtvc.data.repository.NotificationsRepository
import org.jellyfin.androidtvc.data.repository.NotificationsRepositoryImpl
import org.jellyfin.androidtvc.data.repository.UserViewsRepository
import org.jellyfin.androidtvc.data.repository.UserViewsRepositoryImpl
import org.jellyfin.androidtvc.data.service.BackgroundService
import org.jellyfin.androidtvc.ui.ScreensaverViewModel
import org.jellyfin.androidtvc.ui.navigation.Destinations
import org.jellyfin.androidtvc.ui.navigation.NavigationRepository
import org.jellyfin.androidtvc.ui.navigation.NavigationRepositoryImpl
import org.jellyfin.androidtvc.ui.picture.PictureViewerViewModel
import org.jellyfin.androidtvc.ui.playback.PlaybackControllerContainer
import org.jellyfin.androidtvc.ui.playback.nextup.NextUpViewModel
import org.jellyfin.androidtvc.ui.search.SearchFragmentDelegate
import org.jellyfin.androidtvc.ui.search.SearchRepository
import org.jellyfin.androidtvc.ui.search.SearchRepositoryImpl
import org.jellyfin.androidtvc.ui.search.SearchViewModel
import org.jellyfin.androidtvc.ui.startup.ServerAddViewModel
import org.jellyfin.androidtvc.ui.startup.StartupViewModel
import org.jellyfin.androidtvc.ui.startup.UserLoginViewModel
import org.jellyfin.androidtvc.util.MarkdownRenderer
import org.jellyfin.androidtvc.util.sdk.legacy
import org.jellyfin.apiclient.AppInfo
import org.jellyfin.apiclient.android
import org.jellyfin.apiclient.logging.AndroidLogger
import org.jellyfin.sdk.android.androidDevice
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import org.jellyfin.sdk.model.DeviceInfo
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.jellyfin.apiclient.Jellyfin as JellyfinApiClient
import org.jellyfin.sdk.Jellyfin as JellyfinSdk

val defaultDeviceInfo = named("defaultDeviceInfo")

val appModule = module {
	// New SDK
	single(defaultDeviceInfo) { androidDevice(get()) }
	single {
		createJellyfin {
			context = androidContext()

			// Add client info
			clientInfo = ClientInfo("Android TV", BuildConfig.VERSION_NAME)
			deviceInfo = get(defaultDeviceInfo)

			// Change server version
			minimumServerVersion = ServerRepository.minimumServerVersion
		}
	}

	single {
		// Create an empty API instance, the actual values are set by the SessionRepository
		get<JellyfinSdk>().createApi()
	}

	single { get<ApiClient>().ws() }

	single { SocketHandler(get(), get(), get(), get(), get(), get(), get(), get()) }

	// Old apiclient
	single {
		JellyfinApiClient {
			appInfo = AppInfo("Android TV", BuildConfig.VERSION_NAME)
			logger = AndroidLogger()
			android(androidApplication())
		}
	}

	single {
		get<JellyfinApiClient>().createApi(
			device = get<DeviceInfo>(defaultDeviceInfo).legacy()
		)
	}

	// Coil (images)
	single {
		ImageLoader.Builder(androidContext()).apply {
			components {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) add(ImageDecoderDecoder.Factory())
				else add(GifDecoder.Factory())
				add(SvgDecoder.Factory())
			}
		}.build()
	}

	// Non API related
	single { DataRefreshService() }
	single { PlaybackControllerContainer() }

	single<UserRepository> { UserRepositoryImpl() }
	single<UserViewsRepository> { UserViewsRepositoryImpl(get()) }
	single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get(), get()) }
	single<ItemMutationRepository> { ItemMutationRepositoryImpl(get(), get()) }
	single<CustomMessageRepository> { CustomMessageRepositoryImpl() }
	single<NavigationRepository> { NavigationRepositoryImpl(Destinations.home) }
	single<SearchRepository> { SearchRepositoryImpl(get()) }

	viewModel { StartupViewModel(get(), get(), get(), get()) }
	viewModel { UserLoginViewModel(get(), get(), get(), get(defaultDeviceInfo)) }
	viewModel { ServerAddViewModel(get()) }
	viewModel { NextUpViewModel(get(), get(), get(), get()) }
	viewModel { PictureViewerViewModel(get()) }
	viewModel { ScreensaverViewModel(get()) }
	viewModel { SearchViewModel(get()) }

	single { BackgroundService(get(), get(), get(), get(), get()) }

	single { MarkdownRenderer(get()) }

	factory { (context: Context) -> SearchFragmentDelegate(context, get()) }
}
