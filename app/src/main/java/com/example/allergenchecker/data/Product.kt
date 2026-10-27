package com.example.allergenchecker.data

data class Product(
    val id: Int,
    val barcode: String,
    val name: String,
    val brand: String,
    val ingredients: List<Ingredient>
)