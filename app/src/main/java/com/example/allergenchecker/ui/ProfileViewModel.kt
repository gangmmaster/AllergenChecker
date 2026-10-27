package com.example.allergenchecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergenchecker.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val allergens: List<String> = emptyList()
)

class ProfileViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUserProfile().collect { profile ->
                profile?.let {
                    _uiState.update { state ->
                        state.copy(
                            name = it.name,
                            allergens = it.allergens.split(",").map { a -> a.trim() }
                        )
                    }
                }
            }
        }
    }
}