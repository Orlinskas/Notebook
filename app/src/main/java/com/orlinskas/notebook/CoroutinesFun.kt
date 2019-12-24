package com.orlinskas.notebook

import kotlinx.coroutines.*

val job: Job = Job()
val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)



