package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.Response
import kotlinx.coroutines.*

abstract class BaseUseCase {
    abstract suspend fun run(): Response

    operator fun invoke(onResult: (Response) -> Unit = {}) {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        val job = scope.async { run() }
        scope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}