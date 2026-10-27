package com.example.allergenchecker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.allergenchecker.data.db.dao.ProductDao
import com.example.allergenchecker.data.db.dao.UserProfileDao
import com.example.allergenchecker.data.db.entity.IngredientEntity
import com.example.allergenchecker.data.db.entity.ProductEntity
import com.example.allergenchecker.data.db.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        ProductEntity::class,
        IngredientEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun productDao(): ProductDao
}