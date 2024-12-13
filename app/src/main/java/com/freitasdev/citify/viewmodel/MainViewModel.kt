package com.freitasdev.citify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CityRepository) : ViewModel() {
    private val _syncStatus = MutableLiveData<String>()
    val syncStatus: LiveData<String> = _syncStatus

    private val _cities = MutableLiveData<List<CityEntity>>()
    val cities: LiveData<List<CityEntity>> = _cities


    private suspend fun _syncData() {
        _syncStatus.value = "Sincronizando dados..."
        repository.syncCities()
        Log.i("SyncData", "Sincronizar cidades")
        _syncStatus.value = "Dados sincronizados dados com sucesso!"
        delay(1000)
        _syncStatus.value = ""
    }

    private suspend fun _getCities() {
        val citiesResponse = repository.getCities()
        _cities.value = citiesResponse.toMutableList()
    }

    private suspend fun _deleteCity(id: Int) {
        repository.deleteCity(id)
        val updatedList = _cities.value?.filter {
            it.id != id
        }
        _cities.value = updatedList ?: listOf()
    }

    fun deleteCity(id: Int) {
        viewModelScope.launch {
            _deleteCity(id)
        }
    }

    fun getCities() {
        viewModelScope.launch {
            _getCities()
        }
    }

    fun syncData() {
        viewModelScope.launch {
            _syncData()
            _getCities()
        }
    }



}