package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.Response
import com.orlinskas.notebook.interactor.FindActualNotificationUseCase
import com.orlinskas.notebook.interactor.Interactor
import com.orlinskas.notebook.interactor.IteractorCallBack
import com.orlinskas.notebook.mvvm.model.Day
import javax.inject.Inject

class MainViewModel: ViewModel(), IteractorCallBack {
    @Inject lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: MutableLiveData<List<Day>>

    private val interactor: Interactor = FindActualNotificationUseCase(this)

    init {
        app.getComponent().inject(this)
        downloadStatusData.postValue(Enums.DownloadStatus.LOADING)
        interactor.run()
    }

    override fun callBack(response: Response) {
        try {
            daysData.postValue(response.data as List<Day>?)
            connectionStatusData.postValue(when (response.code) {
                200 -> Enums.ConnectionStatus.CONNECTION_DONE
                404 -> Enums.ConnectionStatus.CONNECTION_FAIL
                else -> Enums.ConnectionStatus.CONNECTION_FAIL
            })
        } catch (e: Exception) {
            //error
        } finally {
            downloadStatusData.postValue(Enums.DownloadStatus.READY)
        }
    }
}
