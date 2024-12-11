package com.freitasdev.citify.model.dtos

data class CityDTO(
    val id: Int,
    val nome: String,
    val microrregiao: MicroRegion
)

data class MicroRegion(
    val id: Int,
    val nome: String,
    val mesorregiao: MesoRegion
)

data class MesoRegion(
    val id: Int,
    val nome: String,
    val uf: UF
)

data class UF(
    val id: Int,
    val nome: String,
    val region: Region
)

data class Region(
    val id: Int,
    val sigla: String,
    val nome: String,
)