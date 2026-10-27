package com.example.allergenchecker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val barcode: String,
    val name: String,
    val brand: String
)