package com.marianpusk.qrscanner.ui.main.home


import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import com.marianpusk.qrscanner.database.daos.QRCodesDao
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import kotlinx.coroutines.*
import java.io.File
import java.lang.StringBuilder


class MainViewModel(
    val database: QRCodesDao,
    application: Application)
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

    fun insertQRCode(trainingPlan: QRCodeEntity){
        uiScope.launch {
            insert(trainingPlan)
            update(trainingPlan)
        }
    }

    fun writeFile(activity: FragmentActivity?){
        val codes = qrCodes.value
        uiScope.launch {

            withContext(Dispatchers.IO){


                try {
                    val data = StringBuilder()
                    data.append("QR Code", "type")
                    for (code in codes!!){
                        data.append("\n" + code.value, code.type)
                    }
                    val out = activity!!.applicationContext.openFileOutput("codes.csv",Context.MODE_PRIVATE)
                    out!!.write(data.toString().toByteArray())
                    out.close()
                }catch (e: Exception){
                    e.printStackTrace()
                }


            }
        }

    }

    fun shareCsv(activity: FragmentActivity?){

        val context = activity!!.applicationContext
        val file = File(context.filesDir,"codes.csv")

        val path = FileProvider.getUriForFile(context,"com.marianpusk.qrscanner.fileprovider",file)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION)
        sharingIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
        sharingIntent.type = "*/*"
        sharingIntent.putExtra(
            Intent.EXTRA_STREAM,
            path

        )
        //Toast.makeText(activity.applicationContext, Uri.parse("file://" + file.absolutePath).toString(), Toast.LENGTH_LONG).show()
        activity.startActivity(Intent.createChooser(sharingIntent,"share file"))
    }

    fun shareText(activity: FragmentActivity?){

        val codes = qrCodes.value
        var textToSend = ""
        uiScope.launch {
            withContext(Dispatchers.IO){
                for (code in codes!!){
                    textToSend += code.value + "\n"
                }
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_TEXT,textToSend)
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "QR codes")
                activity!!.startActivity(Intent.createChooser(sharingIntent,"share QR Codes"))
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}