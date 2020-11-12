package com.marianpusk.qrscanner.ui.main.scanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marianpusk.qrscanner.database.daos.QRCodesDao
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import kotlinx.coroutines.*

class ScannerViewModel(
    val database: QRCodesDao,
    application: Application
)
    : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val qrCodes = database.getAllCodes()



    private suspend fun insert(qrCodeEntity: QRCodeEntity) {
        withContext(Dispatchers.IO) {
            database.insert(qrCodeEntity)
        }
    }

    private suspend fun update(trainingPlan: QRCodeEntity){
        withContext(Dispatchers.IO){
            database.update(trainingPlan)
        }
    }


    fun insertQRCode(trainingPlan: QRCodeEntity){
        uiScope.launch {
            insert(trainingPlan)
            update(trainingPlan)
        }
    }

    fun scannedCode(){

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}