package com.example.allergenchecker.data

import com.example.allergenchecker.data.db.dao.ProductDao
import com.example.allergenchecker.data.db.dao.UserProfileDao
import com.example.allergenchecker.data.db.entity.IngredientEntity
import com.example.allergenchecker.data.db.entity.ProductEntity
import com.example.allergenchecker.data.db.entity.ProductWithIngredients
import com.example.allergenchecker.data.db.entity.UserProfileEntity
import com.example.allergenchecker.data.network.NetworkModule
import io.github.xilinjia.krdb.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProductRepository(
    private val productDao: ProductDao,
    private val userProfileDao: UserProfileDao
) {

    fun getAll(): Flow<List<Product>> {
        return productDao.getAllProductsWithIngredients().map { list ->
            list.map { it.toProduct() }
        }
    }

    fun getById(id: Int): Flow<Product?> {
        return productDao.getProductWithIngredients(id).map { it?.toProduct() }
    }

    suspend fun findByBarcode(barcode: String): Product? {
        val local = productDao.getProductByBarcode(barcode)
        if (local != null) {
            val ingredients = productDao.getIngredientsForProduct(local.id).first()
            return Product(
                id = local.id,
                barcode = local.barcode,
                name = local.name,
                brand = local.brand,
                ingredients = ingredients.map {
                    Ingredient(it.name, it.desc, Risk.valueOf(it.risk))
                }
            )
        }

        return try {
            val response = NetworkModule.api.getProduct(barcode)
            if (response.status == 1 && response.product != null) {
                val dto = response.product
                val newId = (productDao.getAllProducts().first().maxOfOrNull { it.id } ?: 0) + 1

                val entity = ProductEntity(
                    id = newId,
                    barcode = barcode,
                    name = dto.name ?: "Без названия",
                    brand = dto.brand ?: "Неизвестный бренд"
                )
                productDao.insertProduct(entity)

                val ingredientsList = dto.ingredients?.map {
                    IngredientEntity(
                        productId = newId,
                        name = it.text ?: "Неизвестно",
                        desc = "",
                        risk = "SAFE"
                    )
                } ?: emptyList()

                if (ingredientsList.isNotEmpty()) {
                    productDao.insertIngredients(ingredientsList)
                }

                Product(
                    id = newId,
                    barcode = barcode,
                    name = entity.name,
                    brand = entity.brand,
                    ingredients = ingredientsList.map { Ingredient(it.name, it.desc, Risk.SAFE) }
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addProduct(name: String, brand: String, barcode: String) {
        val existing = productDao.getProductByBarcode(barcode)
        if (existing != null) return

        val newId = (productDao.getAllProducts().first().maxOfOrNull { it.id } ?: 0) + 1
        productDao.insertProduct(
            ProductEntity(
                id = newId,
                barcode = barcode,
                name = name,
                brand = brand
            )
        )
    }

    suspend fun addProductFromText(name: String, brand: String, barcode: String, scannedText: String, realm: io.github.xilinjia.krdb.Realm) {
        val existing = productDao.getProductByBarcode(barcode)
        if (existing != null) return

        val newId = (productDao.getAllProducts().first().maxOfOrNull { it.id } ?: 0) + 1
        productDao.insertProduct(
            ProductEntity(
                id = newId,
                barcode = barcode,
                name = name,
                brand = brand
            )
        )

        // Получаем аллергены пользователя
        val userProfile = userProfileDao.getProfile().first()
        val userAllergens = userProfile?.allergens?.split(",")?.map { it.trim().lowercase() } ?: emptyList()

        // Получаем реестр добавок из Realm
        val additives = realm.query(com.example.allergenchecker.data.realm.Additive::class).find()
        val ingredientsList = mutableListOf<IngredientEntity>()

        // Ищем совпадения в отсканированном тексте
        additives.forEach { additive ->
            val markerLower = additive.marker.lowercase()

            if (scannedText.lowercase().contains(markerLower)) {
                // Если добавка есть в профиле аллергии — ставим DANGER
                val finalRisk = if (userAllergens.contains(markerLower)) {
                    "DANGER"
                } else {
                    additive.riskLevel
                }

                ingredientsList.add(
                    IngredientEntity(
                        productId = newId,
                        name = additive.marker,
                        desc = additive.description,
                        risk = finalRisk
                    )
                )
            }
        }

        if (ingredientsList.isNotEmpty()) {
            productDao.insertIngredients(ingredientsList)
        }
    }

    private fun ProductWithIngredients.toProduct() = Product(
        id = product.id,
        barcode = product.barcode,
        name = product.name,
        brand = product.brand,
        ingredients = ingredients.map {
            Ingredient(it.name, it.desc, Risk.valueOf(it.risk))
        }
    )

    suspend fun seedTestData() {
        val existingProducts = productDao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            val products = listOf(
                ProductEntity(1, "4601234567890", "Конфеты шоколадные", "Ваня-капитаня"),
                ProductEntity(2, "4609876543210", "Чипсы", "Добрый бульбаш"),
                ProductEntity(3, "4605555555555", "Йогурт натуральный", "Activia")
            )

            val ingredients = listOf(
                IngredientEntity(0, 1, "Шоколад", "Пальмовое масло", "WARNING"),
                IngredientEntity(0, 1, "Лесной орех", "Безвредно", "SAFE"),
                IngredientEntity(0, 2, "Картофель", "Содержит крахмал", "SAFE"),
                IngredientEntity(0, 2, "Соль", "Солёная", "WARNING"),
                IngredientEntity(0, 2, "Консерванты", "Очень вредно", "DANGER"),
                IngredientEntity(0, 3, "Молоко", "Содержит лактозу", "DANGER"),
                IngredientEntity(0, 3, "Закваска", "Полезные бактерии", "SAFE")
            )

            products.forEach { productDao.insertProduct(it) }
            productDao.insertIngredients(ingredients)
        }

        val profile = userProfileDao.getProfile().first()
        if (profile == null) {
            userProfileDao.saveProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Кирилл",
                    allergens = "лактоза,E102,арахис,глютен,e150d,e250"
                )
            )
        }
    }

    fun getUserProfile(): Flow<UserProfileEntity?> {
        return userProfileDao.getProfile()
    }
}