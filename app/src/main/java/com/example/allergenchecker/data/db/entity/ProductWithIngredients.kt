package com.example.allergenchecker.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithIngredients(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val ingredients: List<IngredientEntity>
)