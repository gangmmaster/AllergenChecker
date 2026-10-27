package com.example.allergenchecker.ui.diet

sealed interface DietAnalysisUiState {
    object Idle : DietAnalysisUiState
    object LoadingModel : DietAnalysisUiState
    object Analyzing : DietAnalysisUiState
    data class Success(val result: String) : DietAnalysisUiState
    data class Error(val message: String) : DietAnalysisUiState
}