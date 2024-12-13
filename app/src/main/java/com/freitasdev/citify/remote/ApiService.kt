package com.freitasdev.citify.remote

import com.freitasdev.citify.model.dtos.CityDTO
import retrofit2.Response
import retrofit2.http.GET
import kotlin.annotation.Target as Target1

interface ApiService  {
    @GET("localidades/municipios")
    suspend fun getCities(): Response<List<CityDTO>>
}
