package com.freitasdev.citify.repository

import android.util.Log
import com.freitasdev.citify.data.dao.CityDAO
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.remote.ApiClient


class CityRepository(private val cityDAO: CityDAO) {

    // CREATE
    suspend fun syncCities(){
        try {
            val response = ApiClient().instace.getCities()

            if(!response.isSuccessful) {
                Log.e("CityRepository", "Ocorreu um erro na requisição")
                return
            }

            cityDAO.insertCities(
                response.body()?.map {
                    it.toCityEntity()
                } ?: listOf<CityEntity>()
            )


        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao sincronizar as cidades: ${e.message}")
        }
    }

    // READ
    suspend fun getCities(): List<CityEntity> {
        try {
            val response = cityDAO.getCities()

            if(response.isEmpty()) {
                Log.i("CityRepository", "Nenhuma cidade")
            }

            return response
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao sincronizar as cidades: ${e.message}")
        }

        return listOf<CityEntity>()
    }
}