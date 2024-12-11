package com.freitasdev.citify.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val uf: String,
    val region: String
)