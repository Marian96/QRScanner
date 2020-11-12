package com.marianpusk.qrscanner.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marianpusk.qrscanner.database.entities.QRCodeEntity

@Dao
interface QRCodesDao{

    @Insert
    fun insert(qrcode: QRCodeEntity)

    @Update
    fun update(qrcode: QRCodeEntity)

    @Query("DELETE FROM QRCodeEntity")
    fun clear()

    @Query("SELECT * FROM QRCodeEntity")
    fun getAllCodes(): LiveData<List<QRCodeEntity>>

    @Query("DELETE FROM QRCodeEntity WHERE id = :key")
    fun deleteCode(key: Int)
}