package org.jellyfin.androidtvc.di

import org.jellyfin.androidtvc.util.ImageHelper
import org.koin.dsl.module

val utilsModule = module {
	single { ImageHelper(get()) }
}
