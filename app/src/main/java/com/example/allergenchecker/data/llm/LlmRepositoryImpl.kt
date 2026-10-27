package com.example.allergenchecker.data.llm

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import org.nehuatl.llamacpp.LlamaHelper
import java.io.File

class LlmRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val scope: CoroutineScope
) : LlmRepository {

    private var llamaHelper: LlamaHelper? = null
    private val _llmFlow = MutableSharedFlow<LlamaHelper.LLMEvent>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun loadModel(modelPath: String, contextLength: Int) {
        llamaHelper = LlamaHelper(contentResolver, scope, _llmFlow)
        val fileUri = Uri.fromFile(File(modelPath)).toString()
        llamaHelper?.load(path = fileUri, contextLength = contextLength) { _ -> }
    }

    override fun generateResponse(prompt: String): Flow<String> = flow {
        val helper = llamaHelper ?: throw IllegalStateException("Model not loaded")
        helper.predict(prompt)

        _llmFlow.collect { event ->
            when (event) {
                is LlamaHelper.LLMEvent.Ongoing -> emit(event.word)
                is LlamaHelper.LLMEvent.Done -> { }
                is LlamaHelper.LLMEvent.Error -> throw Exception(event.message)
                is LlamaHelper.LLMEvent.Loaded -> { }
                is LlamaHelper.LLMEvent.Started -> { }
                else -> { }
            }
        }
    }

    override fun release() {
        llamaHelper?.release()
        llamaHelper = null
    }
}