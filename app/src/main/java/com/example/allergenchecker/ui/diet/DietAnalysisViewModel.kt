package com.example.allergenchecker.ui.diet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergenchecker.data.llm.LlmRepository
import com.example.allergenchecker.domain.AnalyzeDietUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class DietAnalysisViewModel(
    application: Application,
    private val llmRepository: LlmRepository,
    private val analyzeDietUseCase: AnalyzeDietUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<DietAnalysisUiState>(DietAnalysisUiState.LoadingModel)
    val uiState: StateFlow<DietAnalysisUiState> = _uiState.asStateFlow()

    private var modelLoaded = false

    init {
        loadModel()
    }

    private fun loadModel() {
        viewModelScope.launch {
            try {
                val modelFile = File(getApplication<Application>().filesDir, "qwen2.5-1.5b-instruct-q4_k_m.gguf")

                if (modelFile.exists()) {
                    llmRepository.loadModel(modelFile.absolutePath)
                    modelLoaded = true
                    _uiState.value = DietAnalysisUiState.Idle
                } else {
                    _uiState.value = DietAnalysisUiState.Error("Модель не найдена в памяти приложения")
                }
            } catch (e: Exception) {
                _uiState.value = DietAnalysisUiState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun analyze(foodList: String, allergens: String) {
        if (!modelLoaded) {
            _uiState.value = DietAnalysisUiState.Error("Модель ещё загружается...")
            return
        }
        viewModelScope.launch {
            _uiState.value = DietAnalysisUiState.Analyzing
            try {
                val result = StringBuilder()
                analyzeDietUseCase(foodList, allergens).collect { token ->
                    result.append(token)
                    _uiState.value = DietAnalysisUiState.Success(result.toString())
                }
            } catch (e: Exception) {
                _uiState.value = DietAnalysisUiState.Error("Ошибка анализа: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        llmRepository.release()
    }
}