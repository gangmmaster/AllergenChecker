package com.example.allergenchecker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.allergenchecker.data.Product
import com.example.allergenchecker.data.Risk
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onScanClick: () -> Unit,
    onScanTextClick: () -> Unit,
    onDietClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История проверок") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Профиль")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = onDietClick,
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = null) },
                    text = { Text("Анализ рациона") }
                )
                ExtendedFloatingActionButton(
                    onClick = onScanTextClick,
                    icon = { Icon(Icons.Default.TextFields, contentDescription = null) },
                    text = { Text("Сканировать текст") }
                )
                ExtendedFloatingActionButton(
                    onClick = onScanClick,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Сканировать штрихкод") }
                )
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Поиск по названию или бренду") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            if (state.products.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ничего не найдено")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.products, key = { it.id }) { product ->
                        ProductCard(product) { onProductClick(product.id) }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    val hasDanger = product.ingredients.any { it.risk == Risk.DANGER }
    val hasWarning = product.ingredients.any { it.risk == Risk.WARNING }
    val bg = when {
        hasDanger -> Color(0xFFFFCDD2)
        hasWarning -> Color(0xFFFFF3E0)
        else -> Color(0xFFE8F5E9)
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(product.name, fontWeight = FontWeight.Bold)
            Text(product.brand, style = MaterialTheme.typography.bodySmall)
        }
    }
}