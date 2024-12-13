package com.freitasdev.citify.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.freitasdev.citify.model.entities.CityEntity

@Dao
interface CityDAO {

    @Query("SELECT * FROM cities")
    suspend fun getCities(): List<CityEntity>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertCities(cities: List<CityEntity>)

//    @Delete
//    suspend fun deleteCity(id: Int)
//
//    @Update
//    suspend fun updateCity(city: CityEntity)
}