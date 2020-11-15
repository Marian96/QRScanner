package com.marianpusk.qrscanner.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.marianpusk.qrscanner.database.entities.GeneratedCodeEntity
import com.marianpusk.qrscanner.database.entities.QRCodeEntity

@Dao
interface GeneratedCodeDao {
    @Insert
    fun insert(qrcode: GeneratedCodeEntity)

    @Update
    fun update(qrcode: GeneratedCodeEntity)

    @Query("DELETE FROM GeneratedCodeEntity")
    fun clear()

    @Query("SELECT * FROM GeneratedCodeEntity")
    fun getAllCodes(): LiveData<List<GeneratedCodeEntity>>

    @Query("DELETE FROM GeneratedCodeEntity WHERE id = :key")
    fun deleteCode(key: Int)
}