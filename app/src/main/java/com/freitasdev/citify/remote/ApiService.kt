package com.freitasdev.citify.remote

import com.freitasdev.citify.model.dtos.CityDTO
import retrofit2.http.GET

interface ApiService {
    @GET("localidades/municipios")
    fun getCities(): List<CityDTO>
}