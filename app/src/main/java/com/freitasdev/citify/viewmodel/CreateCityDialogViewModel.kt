package com.freitasdev.citify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import kotlinx.coroutines.launch

class CreateCityDialogViewModel(private val repository: CityRepository): ViewModel() {
    private suspend fun _createCity(cityEntity: CityEntity) {
        repository.createCity(cityEntity)
    }

    fun createCity(cityEntity: CityEntity) {
       viewModelScope.launch {
           _createCity(cityEntity)
       }
    }
}