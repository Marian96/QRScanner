package com.marianpusk.qrscanner.ui.main.generatecodes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marianpusk.qrscanner.database.daos.GeneratedCodeDao
import com.marianpusk.qrscanner.database.daos.QRCodesDao

class GenerateQrViewModelFactory (
    private val dataSource: GeneratedCodeDao,
    private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GenerateQrViewModel::class.java)) {
                return GenerateQrViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
