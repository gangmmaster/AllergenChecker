package com.example.allergenchecker.data.network

import com.example.allergenchecker.data.network.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): ProductResponse
}