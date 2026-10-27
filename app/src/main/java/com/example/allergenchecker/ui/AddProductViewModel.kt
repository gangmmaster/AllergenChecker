package com.example.allergenchecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergenchecker.data.ProductRepository
import io.github.xilinjia.krdb.Realm
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: ProductRepository,
    val realm: Realm
) : ViewModel() {

    fun addProduct(name: String, brand: String, barcode: String, scannedText: String, onDone: () -> Unit) {
        viewModelScope.launch {
            if (scannedText.isNotBlank()) {
                repository.addProductFromText(name, brand, barcode, scannedText, realm)
            } else {
                repository.addProduct(name, brand, barcode)
            }
            onDone()
        }
    }

    fun findProductByBarcode(barcode: String, onFound: (Int) -> Unit) {
        viewModelScope.launch {
            val existing = repository.findByBarcode(barcode)
            if (existing != null) {
                onFound(existing.id)
            }
        }
    }
}