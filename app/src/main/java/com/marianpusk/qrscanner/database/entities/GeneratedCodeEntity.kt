package com.marianpusk.qrscanner.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeneratedCodeEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var value: String = "",

    @ColumnInfo
    var timestamp: Long = 0,

    @ColumnInfo
    var type: String = ""
)