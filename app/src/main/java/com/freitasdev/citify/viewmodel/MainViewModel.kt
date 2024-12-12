package com.freitasdev.citify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _syncStatus = MutableLiveData<String>()
    val syncStatus: LiveData<String> = _syncStatus


    private suspend fun syncData() {
        _syncStatus.value = "Sincronizando dados..."
        delay(5000)
        _syncStatus.value = "Dados sincronizados dados com sucesso!"
    }

    fun syncDataImpl() {
        viewModelScope.launch {
            syncData()
        }
    }
}