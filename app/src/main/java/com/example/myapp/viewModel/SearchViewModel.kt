import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.Data.Song
import com.example.myapp.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    // MutableStateFlow to hold the UI state
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading) // Start in loading state
    val uiState: StateFlow<UiState> = _uiState

    // Function to search songs based on a query
    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading // Set loading state
            try {
                val response = ApiClient.apiService.searchSongs(query)

                // Check if the response results are null or empty
                if (response.results.isNullOrEmpty()) {
                    _uiState.value = UiState.Error("No results found") // Set error state
                } else {
                    _uiState.value = UiState.Success(response.results) // Set success state
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during the API call
                _uiState.value = UiState.Error("Failed to fetch results: ${e.message}")
            }
        }
    }
}