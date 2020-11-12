package com.marianpusk.qrscanner.ui.main.scanner

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marianpusk.qrscanner.database.daos.QRCodesDao
import com.marianpusk.qrscanner.ui.main.home.MainViewModel

class ScannerViewModelFactory (
    private val dataSource: QRCodesDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            return ScannerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}