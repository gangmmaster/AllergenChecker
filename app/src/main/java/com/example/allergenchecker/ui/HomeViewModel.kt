package com.example.allergenchecker.ui

import androidx.lifecycle.ViewModel
import com.example.allergenchecker.data.testProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(products = testProducts))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        val filtered = if (newQuery.isBlank()) testProducts
        else testProducts.filter {
            it.name.contains(newQuery, ignoreCase = true) ||
                    it.brand.contains(newQuery, ignoreCase = true)
        }
        _uiState.update { it.copy(query = newQuery, products = filtered) }
    }
}