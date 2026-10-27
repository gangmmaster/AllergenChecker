package com.example.allergenchecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergenchecker.data.Product
import com.example.allergenchecker.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        viewModelScope.launch {
            repository.seedTestData()
            repository.getAll().collect { products ->
                allProducts = products
                _uiState.update { it.copy(products = products) }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        val filtered = if (newQuery.isBlank()) allProducts
        else allProducts.filter {
            it.name.contains(newQuery, true) || it.brand.contains(newQuery, true)
        }
        _uiState.update { it.copy(query = newQuery, products = filtered) }
    }
}