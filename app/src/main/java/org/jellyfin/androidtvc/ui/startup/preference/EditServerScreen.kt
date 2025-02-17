package org.jellyfin.androidtvc.ui.startup.preference

import androidx.core.os.bundleOf
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.auth.repository.ServerUserRepository
import org.jellyfin.androidtvc.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtvc.ui.preference.dsl.action
import org.jellyfin.androidtvc.ui.preference.dsl.link
import org.jellyfin.androidtvc.ui.preference.dsl.optionsScreen
import org.jellyfin.androidtvc.ui.startup.StartupViewModel
import org.jellyfin.androidtvc.util.getValue
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.DateFormat
import java.util.Date
import java.util.UUID

class EditServerScreen : OptionsFragment() {
	private val startupViewModel: StartupViewModel by activityViewModel()
	private val serverUserRepository: ServerUserRepository by inject()

	override val rebuildOnResume = true

	override val screen by optionsScreen {
		val serverUUID = requireNotNull(
			requireArguments().getValue<UUID>(ARG_SERVER_UUID)
		) { "Server null or malformed uuid" }

		val server = requireNotNull(startupViewModel.getServer(serverUUID)) { "Server not found" }
		val users = serverUserRepository.getStoredServerUsers(server)

		title = server.name

		if (users.isNotEmpty()) {
			category {
				setTitle(R.string.pref_accounts)

				users.forEach { user ->
					link {
						title = user.name
						icon = R.drawable.ic_user

						val lastUsedDate = Date(user.lastUsed)
						content = context.getString(
							R.string.lbl_user_last_used,
							DateFormat.getDateInstance(DateFormat.MEDIUM).format(lastUsedDate),
							DateFormat.getTimeInstance(DateFormat.SHORT).format(lastUsedDate)
						)

						withFragment<EditUserScreen>(bundleOf(
							EditUserScreen.ARG_SERVER_UUID to server.id,
							EditUserScreen.ARG_USER_UUID to user.id,
						))
					}
				}
			}
		}

		category {
			setTitle(R.string.lbl_server)

			action {
				setTitle(R.string.lbl_remove_server)
				setContent(R.string.lbl_remove_users)
				icon = R.drawable.ic_delete
				onActivate = {
					startupViewModel.deleteServer(serverUUID)

					parentFragmentManager.popBackStack()
				}
			}
		}
	}

	companion object {
		const val ARG_SERVER_UUID = "server_uuid"
	}
}
