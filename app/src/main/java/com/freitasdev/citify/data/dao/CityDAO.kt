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

    @Query("SELECT * FROM cities WHERE uf = 'MS'")
    suspend fun getCities(): List<CityEntity>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("DELETE FROM cities WHERE id = :id")
    suspend fun deleteCity(id: Int = 1)

    @Query("SELECT * FROM cities WHERE id = :id ")
    suspend fun getCity(id: Int = 1): CityEntity

    @Update
    suspend fun updateCity(city: CityEntity)
}