package com.marianpusk.qrscanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marianpusk.qrscanner.database.daos.QRCodesDao
import com.marianpusk.qrscanner.database.entities.QRCodeEntity

@Database(entities = [QRCodeEntity::class],version = 1,exportSchema = false)

abstract class QRCodesDatabase: RoomDatabase(){

    abstract val qrcodeDatabase: QRCodesDao

    companion object {

        @Volatile
        private var INSTANCE: QRCodesDatabase? = null

        fun getInstance(context: Context): QRCodesDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QRCodesDatabase::class.java,
                        "qr_codes_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        //.createFromAsset(DATABASE_DIR)
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}