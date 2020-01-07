package com.orlinskas.notebook.interactor

import kotlinx.coroutines.*

abstract class BaseUseCase<out Response, in Params> {

    abstract suspend fun run(params: Params): Response

    operator fun invoke(params: Params, onResult: (Response) -> Unit = {}) {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        val job = scope.async { run(params) }
        scope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    class NULL
}