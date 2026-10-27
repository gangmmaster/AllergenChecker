package com.example.allergenchecker.data.llm

import kotlinx.coroutines.flow.Flow

interface LlmRepository {
    suspend fun loadModel(modelPath: String, contextLength: Int = 2048)
    fun generateResponse(prompt: String): Flow<String>
    fun release()
}