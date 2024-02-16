package org.jellyfin.androidtvc.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import org.jellyfin.androidtvc.data.model.AppNotification
import org.jellyfin.androidtvc.databinding.ViewCardNotificationBinding

class AppNotificationPresenter : Presenter() {
	override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
		val binding = ViewCardNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return AppNotificationViewHolder(binding)
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
		viewHolder as AppNotificationViewHolder
		item as AppNotification

		viewHolder.binding.message.text = item.message
	}

	override fun onUnbindViewHolder(viewHolder: ViewHolder) {
		viewHolder as AppNotificationViewHolder
	}

	private class AppNotificationViewHolder(val binding: ViewCardNotificationBinding) : ViewHolder(binding.root)
}
