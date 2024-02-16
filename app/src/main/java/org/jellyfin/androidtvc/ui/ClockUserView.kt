package org.jellyfin.androidtvc.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.repository.UserRepository
import org.jellyfin.androidtvc.databinding.ClockUserBugBinding
import org.jellyfin.androidtvc.preference.UserPreferences
import org.jellyfin.androidtvc.preference.constant.ClockBehavior
import org.jellyfin.androidtvc.util.ImageUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClockUserView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	defStyleRes: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes), KoinComponent {
	private val binding: ClockUserBugBinding = ClockUserBugBinding.inflate(LayoutInflater.from(context), this, true)
	private val userPreferences by inject<UserPreferences>()
	private val userRepository by inject<UserRepository>()

	var isVideoPlayer = false
		set(value) {
			field = value
			updateClockVisibility()
		}

	init {
		updateClockVisibility()

		val currentUser = userRepository.currentUser.value

		binding.clockUserImage.load(
			url = currentUser?.let(ImageUtils::getPrimaryImageUrl),
			placeholder = ContextCompat.getDrawable(context, R.drawable.ic_user)
		)

		binding.clockUserImage.isVisible = currentUser != null
	}

	private fun updateClockVisibility() {
		val showClock = userPreferences[UserPreferences.clockBehavior]

		binding.clock.isVisible = when (showClock) {
			ClockBehavior.ALWAYS -> true
			ClockBehavior.NEVER -> false
			ClockBehavior.IN_VIDEO -> isVideoPlayer
			ClockBehavior.IN_MENUS -> !isVideoPlayer
		}
	}
}
