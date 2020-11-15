package com.marianpusk.qrscanner.ui.main.generatecodes

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import com.marianpusk.qrscanner.database.daos.GeneratedCodeDao
import com.marianpusk.qrscanner.database.entities.GeneratedCodeEntity
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import kotlinx.coroutines.*

class GenerateQrViewModel(
    val database: GeneratedCodeDao,
    application: Application
)
    : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val qrCodes = database.getAllCodes()

    private suspend fun deleteQRCode(id: Int){
        withContext(Dispatchers.IO){
            database.deleteCode(id)
        }
    }

    private suspend fun deleteAll(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    private suspend fun insert(generatedCodeEntity: GeneratedCodeEntity) {
        withContext(Dispatchers.IO) {
            database.insert(generatedCodeEntity)
        }
    }

    private suspend fun update(qrCode: GeneratedCodeEntity){
        withContext(Dispatchers.IO){
            database.update(qrCode)
        }
    }

    private suspend fun getAllQRCodes(){
        withContext(Dispatchers.IO){
            database.getAllCodes()
        }
    }



    fun onDelete(id: Int){
        uiScope.launch {
            deleteQRCode(id)
        }
    }

    fun getQRCodes(){
        uiScope.launch {
            getAllQRCodes()
        }
    }



    fun clear(){
        uiScope.launch {
            deleteAll()
        }
    }

    fun insertQRCode(qrCode: GeneratedCodeEntity){
        uiScope.launch {
            insert(qrCode)
            update(qrCode)
        }
    }


    fun shareSingle(activity: FragmentActivity?, code: String){
        uiScope.launch {
            withContext(Dispatchers.IO){

                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_TEXT,code)
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "QR code")
                activity!!.startActivity(Intent.createChooser(sharingIntent,"share QR Code"))
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}