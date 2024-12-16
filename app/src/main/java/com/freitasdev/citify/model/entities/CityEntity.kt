package com.freitasdev.citify.model.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey


@Entity(tableName = "cities", indices = [Index(value = ["name"], unique = true)])
data class CityEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val uf: String,
    val region: String,
    val isDeleted: Boolean = false
)