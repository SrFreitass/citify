package com.freitasdev.citify.model.dtos

import com.freitasdev.citify.model.entities.CityEntity

fun CityDTO.toCityEntity(): CityEntity {
    return CityEntity(
        id = this.id,
        name = this.nome,
        uf = this.microrregiao.mesorregiao.uf.nome,
        region = this.microrregiao.mesorregiao.uf.regiao.nome
    )
}

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
    val regiao: Region
)

data class Region(
    val id: Int,
    val sigla: String,
    val nome: String,
)

