package com.example.allergenchecker.ui

import com.example.allergenchecker.data.Product

data class HomeUiState(
    val query: String = "",
    val products: List<Product> = emptyList()
)