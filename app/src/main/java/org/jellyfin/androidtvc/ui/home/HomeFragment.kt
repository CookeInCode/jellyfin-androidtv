package org.jellyfin.androidtvc.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.repository.SessionRepository
import org.jellyfin.androidtvc.auth.repository.UserRepository
import org.jellyfin.androidtvc.databinding.FragmentHomeBinding
import org.jellyfin.androidtvc.ui.navigation.Destinations
import org.jellyfin.androidtvc.ui.navigation.NavigationRepository
import org.jellyfin.androidtvc.ui.playback.MediaManager
import org.jellyfin.androidtvc.ui.startup.StartupActivity
import org.jellyfin.androidtvc.util.ImageUtils
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!

	private val sessionRepository by inject<SessionRepository>()
	private val userRepository by inject<UserRepository>()
	private val navigationRepository by inject<NavigationRepository>()
	private val mediaManager by inject<MediaManager>()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)

		binding.settings.setOnClickListener {
			navigationRepository.navigate(Destinations.userPreferences)
		}

		binding.switchUsers.setOnClickListener {
			switchUser()
		}

		binding.search.setOnClickListener {
			navigationRepository.navigate(Destinations.search)
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		userRepository.currentUser
			.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
			.onEach { user ->
				if (user != null) {
					binding.switchUsersImage.load(
						url = ImageUtils.getPrimaryImageUrl(user),
						placeholder = ContextCompat.getDrawable(requireContext(), R.drawable.ic_user)
					)
				}
			}.launchIn(viewLifecycleOwner.lifecycleScope)
	}

	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}

	private fun switchUser() {
		mediaManager.clearAudioQueue()
		sessionRepository.destroyCurrentSession()

		// Open login activity
		val selectUserIntent = Intent(activity, StartupActivity::class.java)
		selectUserIntent.putExtra(StartupActivity.EXTRA_HIDE_SPLASH, true)
		// Remove history to prevent user going back to current activity
		selectUserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

		activity?.startActivity(selectUserIntent)
		activity?.finishAfterTransition()
	}
}
