package com.freitasdev.citify.repository

import android.util.Log
import com.freitasdev.citify.data.dao.CityDAO
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.remote.ApiClient


class CityRepository(private val cityDAO: CityDAO) {

    // CREATE
    suspend fun syncCities() {
        try {
            val response = ApiClient().instace.getCities()

            if(!response.isSuccessful) {
                Log.e("CityRepository", "Ocorreu um erro na requisição")
                return
            }

            cityDAO.deleteAllCities()

            cityDAO.insertCities(
                response.body()?.map {
                    it.toCityEntity()
                } ?: listOf<CityEntity>()
            )


        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao sincronizar as cidades: ${e.message}")
        }
    }

    suspend fun createCity(cityEntity: CityEntity) {
        try {
            cityDAO.insertCities(listOf(cityEntity))
        } catch(e: Exception) {
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
            Log.e("CityRepository", "Erro ao requisitar as cidades: ${e.message}")
        }

        return listOf<CityEntity>()
    }


    // DELETE
    suspend fun deleteCity(id: Int) {
        try {
           cityDAO.deleteCity(id)
            Log.i("CityRepository", "A cidade foi corretamente deletada!")
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao deletar a cidade: ${e.message}")
        }
    }

    suspend fun getCityById(id: Int): CityEntity? {
        try {
            val city = cityDAO.getCityById(id)
            Log.i("CityRepository", "A cidade foi encontrada!")
            return city
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao deletar a cidade: ${e.message}")
        }

        return null
    }

    suspend fun getCityByEntity(cityEntity: CityEntity): CityEntity? {
        try {
            val city = cityDAO.getCityByInfo(name =  cityEntity.name, uf = cityEntity.uf, region = cityEntity.region)
            Log.i("CityRepository", "A cidade foi encontrada!")
            return city
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao deletar a cidade: ${e.message}")
        }

        return null
    }

    suspend fun updateCity(cityEntity: CityEntity)  {
        try {
            cityDAO.updateCity(cityEntity)
            Log.i("CityRepository", "A cidade foi alterada!")
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao deletar a cidade: ${e.message}")
        }
    }


    suspend fun getCityByName(name: String): List<CityEntity>? {
        try {
            val cities = cityDAO.getCityByName(name)
            Log.i("CityRepository", "A cidade foi alterada!")
            return cities
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao requisitar a cidade: ${e.message}")
        }

        return null
    }

    suspend fun getCityByRegion(region: String): List<CityEntity>? {
        try {
            val cities = cityDAO.getCityByRegion(region)
            return cities
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao requisitar a cidade: ${e.message}")
        }

        return null
    }

    suspend fun getCityByState(state: String): List<CityEntity>? {
        try {
            val cities = cityDAO.getCityByState(state)
            return cities
        } catch (e: Exception) {
            Log.e("CityRepository", "Erro ao requisitar a cidade: ${e.message}")
        }

        return null
    }
}