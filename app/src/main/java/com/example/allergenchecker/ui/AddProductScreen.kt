package com.example.allergenchecker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    initialBarcode: String = "",
    initialText: String = "",
    onBack: () -> Unit,
    onProductFound: (Int) -> Unit,
    viewModel: AddProductViewModel = koinViewModel()
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf(initialBarcode) }
    var ingredientsText by remember { mutableStateOf(initialText) }

    LaunchedEffect(initialBarcode) {
        if (initialBarcode.isNotBlank()) {
            viewModel.findProductByBarcode(initialBarcode) { id ->
                onProductFound(id)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новый продукт") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Штрих-код") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Бренд") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = ingredientsText,
                onValueChange = { ingredientsText = it },
                label = { Text("Состав (текст)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && brand.isNotBlank() && barcode.isNotBlank()) {
                        viewModel.addProduct(name, brand, barcode, ingredientsText) { onBack() }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить", fontWeight = FontWeight.Bold)
            }
        }
    }
}