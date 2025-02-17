package org.jellyfin.androidtvc.ui.startup.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.model.ApiClientErrorLoginState
import org.jellyfin.androidtvc.auth.model.AuthenticatedState
import org.jellyfin.androidtvc.auth.model.AuthenticatingState
import org.jellyfin.androidtvc.auth.model.PrivateUser
import org.jellyfin.androidtvc.auth.model.RequireSignInState
import org.jellyfin.androidtvc.auth.model.Server
import org.jellyfin.androidtvc.auth.model.ServerUnavailableState
import org.jellyfin.androidtvc.auth.model.ServerVersionNotSupported
import org.jellyfin.androidtvc.auth.model.User
import org.jellyfin.androidtvc.auth.repository.AuthenticationRepository
import org.jellyfin.androidtvc.auth.repository.ServerRepository
import org.jellyfin.androidtvc.auth.repository.ServerUserRepository
import org.jellyfin.androidtvc.data.service.BackgroundService
import org.jellyfin.androidtvc.databinding.FragmentServerBinding
import org.jellyfin.androidtvc.ui.ServerButtonView
import org.jellyfin.androidtvc.ui.card.DefaultCardView
import org.jellyfin.androidtvc.ui.startup.StartupViewModel
import org.jellyfin.androidtvc.util.ListAdapter
import org.jellyfin.androidtvc.util.MarkdownRenderer
import org.jellyfin.sdk.model.serializer.toUUIDOrNull
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ServerFragment : Fragment() {
	companion object {
		const val ARG_SERVER_ID = "server_id"
	}

	private val startupViewModel: StartupViewModel by activityViewModel()
	private val markdownRenderer: MarkdownRenderer by inject()
	private val authenticationRepository: AuthenticationRepository by inject()
	private val serverUserRepository: ServerUserRepository by inject()
	private val backgroundService: BackgroundService by inject()
	private var _binding: FragmentServerBinding? = null
	private val binding get() = _binding!!

	private val serverIdArgument get() = arguments?.getString(ARG_SERVER_ID)?.ifBlank { null }?.toUUIDOrNull()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val server = serverIdArgument?.let(startupViewModel::getServer)

		if (server == null) {
			navigateFragment<SelectServerFragment>(keepToolbar = true, keepHistory = false)
			return null
		}

		_binding = FragmentServerBinding.inflate(inflater, container, false)

		val userAdapter = UserAdapter(requireContext(), server, startupViewModel, authenticationRepository, serverUserRepository)
		userAdapter.onItemPressed = { user ->
			startViewModelAuthenticate(server, user)
		}
		binding.users.adapter = userAdapter

		// Automatically add user logic
		navigateFragment<UserLoginFragment>(
			args = bundleOf(
				UserLoginFragment.ARG_SERVER_ID to server.id.toString(),
				UserLoginFragment.ARG_USERNAME to null
			)
		)

		setupViewElements(server)

		return binding.root
	}


	private fun setupViewElements(server: Server) {
		binding.addUserButton.setOnClickListener {
			navigateFragment<UserLoginFragment>(
				args = bundleOf(
					UserLoginFragment.ARG_SERVER_ID to server.id.toString(),
					UserLoginFragment.ARG_USERNAME to null
				)
			)
		}

		// Other setup related to view elements goes here...
	}

	private fun startViewModelAuthenticate(server: Server, user: User) {
		startupViewModel.authenticate(server, user).onEach { state ->
			when (state) {
				// Ignored states
				AuthenticatingState -> Unit
				AuthenticatedState -> Unit
				// Actions
				RequireSignInState -> navigateFragment<UserLoginFragment>(bundleOf(
					UserLoginFragment.ARG_SERVER_ID to server.id.toString(),
					UserLoginFragment.ARG_USERNAME to user.name,
				))
				// Errors
				ServerUnavailableState,
				is ApiClientErrorLoginState -> Toast.makeText(context, R.string.server_connection_failed, Toast.LENGTH_LONG).show()

				is ServerVersionNotSupported -> Toast.makeText(
					context,
					getString(
						R.string.server_issue_outdated_version,
						state.server.version,
						ServerRepository.recommendedServerVersion.toString()
					),
					Toast.LENGTH_LONG
				).show()
			}
		}.launchIn(lifecycleScope)
	}

	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}

	private fun onServerChange(server: Server) {
		binding.loginDisclaimer.text = server.loginDisclaimer?.let { markdownRenderer.toMarkdownSpanned(it) }

		binding.serverButton.apply {
			state = ServerButtonView.State.EDIT
			name = server.name
			address = server.address
			version = server.version
		}

		binding.addUserButton.setOnClickListener {
			navigateFragment<UserLoginFragment>(
				args = bundleOf(
					UserLoginFragment.ARG_SERVER_ID to server.id.toString(),
					UserLoginFragment.ARG_USERNAME to null
				)
			)
		}

		binding.serverButton.setOnClickListener {
			navigateFragment<SelectServerFragment>(keepToolbar = true)
		}

		if (!server.versionSupported) {
			binding.notification.isVisible = true
			binding.notification.text = getString(
				R.string.server_unsupported_notification,
				server.version,
				ServerRepository.recommendedServerVersion.toString(),
			)
		} else if (!server.setupCompleted) {
			binding.notification.isVisible = true
			binding.notification.text = getString(R.string.server_setup_incomplete)
		} else {
			binding.notification.isGone = true
		}
	}

	private inline fun <reified F : Fragment> navigateFragment(
		args: Bundle = bundleOf(),
		keepToolbar: Boolean = false,
		keepHistory: Boolean = true,
	) {
		requireActivity()
			.supportFragmentManager
			.commit {
				if (keepToolbar) {
					replace<StartupToolbarFragment>(R.id.content_view)
					add<F>(R.id.content_view, null, args)
				} else {
					replace<F>(R.id.content_view, null, args)
				}

				if (keepHistory) addToBackStack(null)
			}
	}

	override fun onResume() {
		super.onResume()

		startupViewModel.reloadStoredServers()
		backgroundService.clearBackgrounds()

		val server = serverIdArgument?.let(startupViewModel::getServer)
		if (server != null) startupViewModel.loadUsers(server)
		else navigateFragment<SelectServerFragment>(keepToolbar = true)
	}

	private class UserAdapter(
		private val context: Context,
		private val server: Server,
		private val startupViewModel: StartupViewModel,
		private val authenticationRepository: AuthenticationRepository,
		private val serverUserRepository: ServerUserRepository,
	) : ListAdapter<User, UserAdapter.ViewHolder>() {
		var onItemPressed: (User) -> Unit = {}

		override fun areItemsTheSame(old: User, new: User): Boolean = old.id == new.id

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val cardView = DefaultCardView(context).apply {
				setSize(DefaultCardView.Size.SQUARE)
			}

			return ViewHolder(cardView)
		}

		override fun onBindViewHolder(holder: ViewHolder, user: User) {
			holder.cardView.title = user.name
			holder.cardView.setImage(
				url = startupViewModel.getUserImage(server, user),
				placeholder = ContextCompat.getDrawable(context, R.drawable.tile_user)
			)
			holder.cardView.setPopupMenu {
				// Logout button
				if (user is PrivateUser && user.accessToken != null) {
					item(context.getString(R.string.lbl_sign_out)) {
						authenticationRepository.logout(user)
					}
				}

				// Remove button
				if (user is PrivateUser) {
					item(context.getString(R.string.lbl_remove)) {
						serverUserRepository.deleteStoredUser(user)
						startupViewModel.loadUsers(server)
					}
				}
			}

			holder.cardView.setOnClickListener {
				onItemPressed(user)
			}
		}

		private class ViewHolder(
			val cardView: DefaultCardView,
		) : RecyclerView.ViewHolder(cardView)
	}
}

