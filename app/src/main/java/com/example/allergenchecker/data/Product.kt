package com.example.allergenchecker.data

data class Product(
    val id: Int,
    val name: String,
    var brand: String,
    val ingredients: List<Ingredient>
)