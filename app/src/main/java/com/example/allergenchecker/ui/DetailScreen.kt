package com.example.allergenchecker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.allergenchecker.data.Ingredient
import com.example.allergenchecker.data.Risk
import com.example.allergenchecker.data.testProducts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Int, onBack: () -> Unit) {
    val product = testProducts.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (product == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Не найдено")
            }
            return@Scaffold
        }

        val hasDanger = product.ingredients.any { it.risk == Risk.DANGER }
        val hasWarning = product.ingredients.any { it.risk == Risk.WARNING }
        val bg = when {
            hasDanger -> Color(0xFFFFCDD2)
            hasWarning -> Color(0xFFFFF3E0)
            else -> Color(0xFFE8F5E9)
        }
        val verdict = when {
            hasDanger -> "ПРОТИВОПОКАЗАНО"
            hasWarning -> "ЕСТЬ ДОБАВКИ"
            else -> "БЕЗОПАСНО"
        }

        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(product.brand, style = MaterialTheme.typography.bodyMedium)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = bg)
                ) {
                    Text(verdict, Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                }
            }

            item {
                Text(
                    "Состав (${product.ingredients.size})",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(product.ingredients) { ing ->
                IngredientRow(ing)
            }
        }
    }
}

@Composable
fun IngredientRow(ing: Ingredient) {
    val ingBg = when (ing.risk) {
        Risk.DANGER -> Color(0xFFFFCDD2)
        Risk.WARNING -> Color(0xFFFFF3E0)
        Risk.SAFE -> Color(0xFFE8F5E9)
    }
    val label = when (ing.risk) {
        Risk.DANGER -> "Опасно"
        Risk.WARNING -> "Внимание"
        Risk.SAFE -> "ОК"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ingBg)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(ing.name, fontWeight = FontWeight.Bold)
            Text(ing.desc, style = MaterialTheme.typography.bodySmall)
        }
        Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
    }
}