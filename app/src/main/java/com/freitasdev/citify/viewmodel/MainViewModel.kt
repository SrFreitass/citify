package com.freitasdev.citify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import com.freitasdev.citify.utils.EspressoIdlingResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CityRepository) : ViewModel() {
    private val _syncStatus = MutableLiveData<String>()
    val syncStatus: LiveData<String> = _syncStatus

    private val _syncStatusBool = MutableLiveData(false)
    val syncStatusBool: LiveData<Boolean> = _syncStatusBool

    private val _cities = MutableLiveData<List<CityEntity>>()
    val cities: LiveData<List<CityEntity>> = _cities


    private suspend fun _syncData() {
        EspressoIdlingResource.increment()
        _syncStatus.value = "Sincronizando dados..."

        val cities = repository.getCities()

        if(cities.isNotEmpty()) {
            _syncStatus.value = "Dados já sincronizados!"
            _syncStatusBool.value = true
            delay(1000)
            _syncStatus.value = ""
            return
        }

        repository.syncCities()
        Log.i("SyncData", "Sincronizar cidades")
        _syncStatus.value = "Dados sincronizados dados com sucesso!"
        delay(1000)
        _syncStatus.value = ""
        EspressoIdlingResource.decrement()
    }

    private suspend fun _getCities() {
        EspressoIdlingResource.increment()
        val citiesResponse = repository.getCities()
        _cities.value = citiesResponse.toMutableList()
        EspressoIdlingResource.decrement()
    }

    private suspend fun _deleteCity(id: Int) {
        EspressoIdlingResource.increment()
        repository.deleteCity(id)
        val updatedList = _cities.value?.filter {
            it.id != id
        }
        _cities.value = updatedList ?: listOf()
        EspressoIdlingResource.decrement()
    }

    private suspend fun _getCityByName(name: String) {
        EspressoIdlingResource.increment()
        val cities = repository.getCityByName("%$name%") ?: listOf()
        _cities.value = cities
        EspressoIdlingResource.decrement()
    }

    private suspend fun _getCityByRegion(region: String) {
        EspressoIdlingResource.increment()
        _cities.value = repository.getCityByRegion(region) ?: listOf()
        EspressoIdlingResource.decrement()
    }

    private suspend fun _getCityByState(state: String) {
        _cities.value = repository.getCityByState(state) ?: listOf()
    }


    fun getCityByState(state: String) {
        viewModelScope.launch {
            _getCityByState(state)
        }
    }

    fun getCityByRegion(region: String) {
        viewModelScope.launch {
            _getCityByRegion(region)
        }
    }

    fun getCityByName(name: String) {
        viewModelScope.launch {
            _getCityByName(name)
        }
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