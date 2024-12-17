package com.freitasdev.citify.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.freitasdev.citify.model.entities.CityEntity

@Dao
interface CityDAO {

    @Query("SELECT * FROM cities WHERE isDeleted = 0 ORDER BY id DESC")
    suspend fun getCities(): List<CityEntity>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("UPDATE cities SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteCity(id: Int = 1)

    @Query("UPDATE cities SET isDeleted = 1")
    suspend fun deleteAllCities()

    @Query("SELECT * FROM cities WHERE id = :id ")
    suspend fun getCityById(id: Int = 1): CityEntity

    @Query("SELECT * FROM cities WHERE LOWER(name) = LOWER(:name) AND uf = :uf AND region = :region")
    suspend fun getCityByInfo(name: String, uf: String, region: String): CityEntity

    @Update
    suspend fun updateCity(city: CityEntity)
}