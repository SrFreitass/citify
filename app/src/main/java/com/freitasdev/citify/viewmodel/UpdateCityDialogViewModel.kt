package com.freitasdev.citify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import com.freitasdev.citify.utils.EspressoIdlingResource
import kotlinx.coroutines.launch

class UpdateCityDialogViewModel(private val repository: CityRepository): ViewModel() {
    private val _city = MutableLiveData<CityEntity?>()
    val city: LiveData<CityEntity?> = _city


    private suspend fun _getCityById(id: Int) {
        val cityResponse = repository.getCityById(id)
        _city.value = cityResponse
    }

    private suspend fun _updateCity(cityEntity: CityEntity) {
        EspressoIdlingResource.increment()
        repository.updateCity(cityEntity)
        EspressoIdlingResource.decrement()
    }

    fun updateCity(cityEntity: CityEntity) {
        viewModelScope.launch {
            _updateCity(cityEntity)
        }
    }


    fun getCityById(id: Int) {
        viewModelScope.launch {
            _getCityById(id)
        }
    }

}