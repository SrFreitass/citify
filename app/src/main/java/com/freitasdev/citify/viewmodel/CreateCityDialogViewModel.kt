package com.freitasdev.citify.viewmodel

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freitasdev.citify.R
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import kotlinx.coroutines.launch

class CreateCityDialogViewModel(private val repository: CityRepository): ViewModel() {
    private val _createdStatus = MutableLiveData<Boolean>()
    val createdStatus: LiveData<Boolean> = _createdStatus

    private suspend fun _getCityByEntity(cityEntity: CityEntity): Boolean {
       val city = repository.getCityByEntity(cityEntity)

        return city != null
    }

    private suspend fun _createCity(cityEntity: CityEntity) {

        if(_getCityByEntity(cityEntity)) {
            _createdStatus.value = false
            return
        }

        repository.createCity(cityEntity)
        _createdStatus.value = true
    }

    fun createCity(cityEntity: CityEntity) {
       viewModelScope.launch {
           _createCity(cityEntity)
       }
    }
}