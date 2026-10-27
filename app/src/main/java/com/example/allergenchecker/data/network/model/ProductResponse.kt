package com.example.allergenchecker.data.network.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("product") val product: ProductDto?
)

data class ProductDto(
    @SerializedName("product_name") val name: String?,
    @SerializedName("brands") val brand: String?,
    @SerializedName("ingredients_text") val ingredientsText: String?,
    @SerializedName("ingredients") val ingredients: List<IngredientDto>?
)

data class IngredientDto(
    @SerializedName("text") val text: String?
)