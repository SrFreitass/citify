package com.freitasdev.citify.model.dtos

import com.freitasdev.citify.model.entities.CityEntity
import com.google.gson.annotations.SerializedName

data class CityDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("nome") val name: String,
    @SerializedName("microrregiao") val microregion: Microregion
) {
    fun toCityEntity(): CityEntity {
        return CityEntity(
            name = this.name,
            uf = this.microregion.mesoregion.state.acronym,
            region = this.microregion.mesoregion.state.region.name
        )
    }
}

data class Microregion(
    @SerializedName("id") val id: Int,
    @SerializedName("nome") val name: String,
    @SerializedName("mesorregiao") val mesoregion: Mesoregion
)

data class Mesoregion(
    @SerializedName("id") val id: Int,
    @SerializedName("nome") val name: String,
    @SerializedName("UF") val state: State
)

data class State(
    @SerializedName("id") val id: Int,
    @SerializedName("sigla") val acronym: String,
    @SerializedName("nome") val name: String,
    @SerializedName("regiao") val region: Region
)

data class Region(
    @SerializedName("id") val id: Int,
    @SerializedName("sigla") val acronym: String,
    @SerializedName("nome") val name: String
)
