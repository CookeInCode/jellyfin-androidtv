package org.jellyfin.androidtvc.ui.startup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.model.ConnectedState
import org.jellyfin.androidtvc.auth.model.ConnectingState
import org.jellyfin.androidtvc.auth.model.UnableToConnectState
import org.jellyfin.androidtvc.databinding.FragmentServerAddBinding
import org.jellyfin.androidtvc.ui.startup.ServerAddViewModel
import org.jellyfin.androidtvc.util.getSummary
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServerAddFragment : Fragment() {
	companion object {
		const val ARG_SERVER_ADDRESS = "server_address"
	}

	private val startupViewModel: ServerAddViewModel by viewModel()
	private var _binding: FragmentServerAddBinding? = null
	private val binding get() = _binding!!

	private val serverAddressArgument get() = arguments?.getString(ARG_SERVER_ADDRESS)?.ifBlank { null }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentServerAddBinding.inflate(inflater, container, false)

		with(binding.address) {
			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE -> {
						submitAddress()
						true
					}

					else -> false
				}
			}
		}

		with(binding.confirm) {
			setOnClickListener { submitAddress() }
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Hardcoded server address
		val providedServerAddress = "192.168.127.5:8096"

		// Set the server address in the address field
		binding.address.setText(providedServerAddress)

		// Disable the address field to prevent user modification
		binding.address.isEnabled = false

		// Immediately initiate the connection process
		submitAddress()

		startupViewModel.state.onEach { state ->
			when (state) {
				is ConnectingState -> {
					binding.address.isEnabled = false
					binding.confirm.isEnabled = false
					binding.error.text = getString(R.string.server_connecting, state.address)
				}
				is UnableToConnectState -> {
					binding.address.isEnabled = true
					binding.confirm.isEnabled = true
					// Update state text
					binding.error.text = getString(
						R.string.server_connection_failed_candidates,
						state.addressCandidates
							.map { "${it.key} - ${it.value.getSummary(requireContext())}" }
							.joinToString(prefix = "\n", separator = "\n")
					)
				}
				is ConnectedState -> parentFragmentManager.commit {
					// Open server view
					replace<StartupToolbarFragment>(R.id.content_view)
					add<ServerFragment>(
						R.id.content_view,
						null,
						bundleOf(
							ServerFragment.ARG_SERVER_ID to state.id.toString()
						)
					)
				}
				null -> Unit
			}
		}.launchIn(lifecycleScope)
	}


	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}

	private fun submitAddress() = when {
		binding.address.text.isNotBlank() -> startupViewModel.addServer(binding.address.text.toString())
		else -> binding.error.setText(R.string.server_field_empty)
	}
}
