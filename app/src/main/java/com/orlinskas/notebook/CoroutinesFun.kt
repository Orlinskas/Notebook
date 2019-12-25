package com.orlinskas.notebook

import kotlinx.coroutines.*

val job: Job = Job()
val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)
val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)



