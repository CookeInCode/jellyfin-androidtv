package org.jellyfin.androidtvc.ui

fun interface ValueChangedListener<T> {
	fun onValueChanged(value: T)
}
