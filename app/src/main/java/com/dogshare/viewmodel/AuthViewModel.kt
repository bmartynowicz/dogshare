import android.content.Context // Correct import for Android context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.utils.AuthUtils
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    // Expose the isLoggedIn StateFlow from AuthUtils
    val isLoggedIn: StateFlow<Boolean> = AuthUtils.isLoggedIn

    init {
        // Initialize SharedPreferences and StateFlow
        AuthUtils.initialize(context)
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // No need to update _isLoggedIn in this ViewModel; it’s handled by AuthUtils
            // This method just ensures initialization logic if needed
        }
    }

    fun updateLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            // Update the login status using AuthUtils
            AuthUtils.setUserLoggedIn(isLoggedIn)
        }
    }
}
