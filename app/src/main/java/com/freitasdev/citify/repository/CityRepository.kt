package com.freitasdev.citify.repository

import android.util.Log
import com.freitasdev.citify.data.dao.CityDAO
import com.freitasdev.citify.model.dtos.toCityEntity
import com.freitasdev.citify.remote.ApiClient


class CityRepository(private val cityDAO: CityDAO) {
    // CREATE Cities
    suspend fun syncCities(){
        try {
            val response = ApiClient().instace.getCities()

            if(response.isEmpty()) {
                Log.i("CityRepository", "O corpo de resposta está vazio")
                return
            }

            cityDAO.insertCities(
                response.map {
                it.toCityEntity()
            }
            )


        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao sincronizar as cidades: ${e.message}")
        }
    }
}