package org.jellyfin.androidtvc.ui.startup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.model.ApiClientErrorLoginState
import org.jellyfin.androidtvc.auth.model.AuthenticatedState
import org.jellyfin.androidtvc.auth.model.AuthenticatingState
import org.jellyfin.androidtvc.auth.model.RequireSignInState
import org.jellyfin.androidtvc.auth.model.ServerUnavailableState
import org.jellyfin.androidtvc.auth.model.ServerVersionNotSupported
import org.jellyfin.androidtvc.auth.repository.ServerRepository
import org.jellyfin.androidtvc.databinding.FragmentUserLoginCredentialsBinding
import org.jellyfin.androidtvc.ui.startup.UserLoginViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class UserLoginCredentialsFragment : Fragment() {
	private val userLoginViewModel: UserLoginViewModel by activityViewModel()
	private var _binding: FragmentUserLoginCredentialsBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentUserLoginCredentialsBinding.inflate(inflater, container, false)

		with(binding.username) {
			// Prefill username
			if (userLoginViewModel.forcedUsername != null) {
				isFocusable = false
				isEnabled = false
				setText(userLoginViewModel.forcedUsername)
			}
		}

		with(binding.password) {
			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE -> {
						loginWithCredentials() // Call loginWithCredentials() when the "Done" action is triggered
						true
					}
					else -> false
				}
			}
		}

		with(binding.confirm) {
			setOnClickListener { loginWithCredentials() } // Call loginWithCredentials() when the "Confirm" button is clicked
		}

		// Call loginWithCredentials() when the fragment view is created
		loginWithCredentials()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Set focus
		if (binding.username.isFocusable) binding.username.requestFocus()
		else binding.password.requestFocus()

		// Populate username and password fields
		val hardcodedUsername = "USERNAME_HERE"
		val hardcodedPassword = "PASSWD_HERE"

		binding.username.setText(hardcodedUsername)
		binding.password.setText(hardcodedPassword)

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

		// Perform login action
		loginWithCredentials()
	}

	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}

	private fun loginWithCredentials() {
		when {
			binding.username.text.isNotBlank() -> lifecycleScope.launch {
				userLoginViewModel.login(
					binding.username.text.toString(),
					binding.password.text.toString()
				)
			}

			else -> binding.error.setText(R.string.login_username_field_empty)
		}
	}
}
