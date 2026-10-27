package com.example.allergenchecker.domain

import com.example.allergenchecker.data.llm.LlmRepository
import kotlinx.coroutines.flow.Flow

class AnalyzeDietUseCase(private val llmRepository: LlmRepository) {

    operator fun invoke(foodList: String, allergens: String): Flow<String> {
        val prompt = """
            Ты – диетолог. Проанализируй рацион за день. 
            Норма: 2000 ккал, БЖУ 90/70/250г. 
            Аллергии: $allergens. 
            Съедено: $foodList. 
            Посчитай КБЖУ, сравни с нормой, найди скрытые аллергены, дай 2-3 рекомендации. 
            Ответ: таблица БЖУ + список аллергенов + вывод.
        """.trimIndent()

        return llmRepository.generateResponse(prompt)
    }
}