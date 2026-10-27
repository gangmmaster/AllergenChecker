package com.example.allergenchecker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.allergenchecker.data.db.entity.IngredientEntity
import com.example.allergenchecker.data.db.entity.ProductEntity
import com.example.allergenchecker.data.db.entity.ProductWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Transaction
    @Query("SELECT * FROM products")
    fun getAllProductsWithIngredients(): Flow<List<ProductWithIngredients>>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductWithIngredients(id: Int): Flow<ProductWithIngredients?>

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM ingredients WHERE productId = :productId")
    fun getIngredientsForProduct(productId: Int): Flow<List<IngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)
}