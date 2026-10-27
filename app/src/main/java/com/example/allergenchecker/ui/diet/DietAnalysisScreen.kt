package com.example.allergenchecker.ui.diet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietAnalysisScreen(
    viewModel: DietAnalysisViewModel = koinViewModel()
) {
    var foodList by remember { mutableStateOf("") }
    var allergens by remember { mutableStateOf("лактоза, арахис") }
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Анализ рациона (On-Device AI)", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = foodList,
            onValueChange = { foodList = it },
            label = { Text("Что съедено за день") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = allergens,
            onValueChange = { allergens = it },
            label = { Text("Аллергии") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { viewModel.analyze(foodList, allergens) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Анализировать")
        }

        when (val s = state) {
            is DietAnalysisUiState.LoadingModel -> {
                Column {
                    CircularProgressIndicator()
                    Text("Загрузка нейросети в память... Подождите.")
                }
            }
            is DietAnalysisUiState.Analyzing -> {
                Column {
                    CircularProgressIndicator()
                    Text("Анализ рациона...")
                }
            }
            is DietAnalysisUiState.Success -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(s.result, modifier = Modifier.padding(16.dp))
                }
            }
            is DietAnalysisUiState.Error -> {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}