import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _user = MutableStateFlow(
        UserProfileUi(
            avatarUrl = null,
            name = "Ünsal Qasımli",
            nickname = "unsaldev",
            email = "unsal@example.com",
            password = "" // leave blank for security
        )
    )
    val user: StateFlow<UserProfileUi> = _user

    fun updateUser(updated: UserProfileUi) {
        _user.value = updated
        // TODO: Save to backend/Firebase
    }

    fun updateAvatar(newUrl: String) {
        _user.value = _user.value.copy(avatarUrl = newUrl)
        // TODO: Upload new avatar and update backend
    }

    fun signOut() {
        // TODO: Implement sign out
    }
}
