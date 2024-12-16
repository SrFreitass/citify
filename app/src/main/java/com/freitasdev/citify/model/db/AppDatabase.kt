package com.freitasdev.citify.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.freitasdev.citify.data.dao.CityDAO
import com.freitasdev.citify.model.entities.CityEntity

@Database(entities = [CityEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDAO

    companion object {
        @Volatile
        private var instace: AppDatabase? = null

        fun getInstace(context: Context): AppDatabase = instace ?: synchronized(this) {
            instace ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database")
                .fallbackToDestructiveMigration()
                .build().also { instace = it }
        }
    }

}

