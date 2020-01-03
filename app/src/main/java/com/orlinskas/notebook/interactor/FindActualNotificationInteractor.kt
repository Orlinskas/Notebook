package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.builder.DaysBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FindActualNotificationInteractor : BaseInteractor(), Interactor {
    override fun execute() {
        val currentDateMillis = System.currentTimeMillis()

        scope.launch {
            val local = async {
                repository.findActualLocal(currentDateMillis)
            }

            daysData.postValue(DaysBuilder(local.await()).findActual())

            val remote = async {
                repository.findActualRemote(currentDateMillis)
            }

            daysData.postValue(DaysBuilder(synchronizer.sync(local.await(), remote.await())).findActual())
        }
    }
}