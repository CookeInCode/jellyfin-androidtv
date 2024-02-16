package org.jellyfin.androidtvc.ui.startup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.model.ApiClientErrorLoginState
import org.jellyfin.androidtvc.auth.model.AuthenticatedState
import org.jellyfin.androidtvc.auth.model.AuthenticatingState
import org.jellyfin.androidtvc.auth.model.ConnectedQuickConnectState
import org.jellyfin.androidtvc.auth.model.PendingQuickConnectState
import org.jellyfin.androidtvc.auth.model.RequireSignInState
import org.jellyfin.androidtvc.auth.model.ServerUnavailableState
import org.jellyfin.androidtvc.auth.model.ServerVersionNotSupported
import org.jellyfin.androidtvc.auth.model.UnavailableQuickConnectState
import org.jellyfin.androidtvc.auth.model.UnknownQuickConnectState
import org.jellyfin.androidtvc.auth.repository.ServerRepository
import org.jellyfin.androidtvc.databinding.FragmentUserLoginQuickConnectBinding
import org.jellyfin.androidtvc.ui.startup.UserLoginViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class UserLoginQuickConnectFragment : Fragment() {
	private val userLoginViewModel: UserLoginViewModel by activityViewModel()
	private var _binding: FragmentUserLoginQuickConnectBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentUserLoginQuickConnectBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		userLoginViewModel.clearLoginState()

		// Initialize & react to Quick Connect specific state
		lifecycleScope.launch {
			userLoginViewModel.initiateQuickconnect()

			userLoginViewModel.quickConnectState.collect { state ->
				when (state) {
					is PendingQuickConnectState -> {
						binding.quickConnectCode.text = state.code.formatCode()
						binding.loading.isVisible = false
					}

					UnavailableQuickConnectState,
					UnknownQuickConnectState,
					ConnectedQuickConnectState -> binding.loading.isVisible = true
				}
			}
		}

		// React to login state
		userLoginViewModel.loginState.onEach { state ->
			when (state) {
				is ServerVersionNotSupported -> binding.error.setText(getString(
					R.string.server_issue_outdated_version,
					state.server.version,
					ServerRepository.recommendedServerVersion.toString()
				))

				AuthenticatingState -> binding.error.setText(R.string.login_authenticating)
				RequireSignInState -> binding.error.setText(R.string.login_invalid_credentials)
				ServerUnavailableState,
				is ApiClientErrorLoginState -> binding.error.setText(R.string.login_server_unavailable)
				// Do nothing because the activity will respond to the new session
				AuthenticatedState -> Unit
				// Not initialized
				null -> Unit
			}
		}.launchIn(lifecycleScope)
	}

	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}

	/**
	 * Add space after every 3 characters so "420420" becomes "420 420".
	 */
	private fun String.formatCode() = buildString {
		@Suppress("MagicNumber")
		val interval = 3
		this@formatCode.forEachIndexed { index, character ->
			if (index != 0 && index % interval == 0) append(" ")
			append(character)
		}
	}
}
