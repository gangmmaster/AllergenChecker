package com.example.allergenchecker.data

val testProducts = listOf(
    Product(
        id = 1,
        name = "Конфеты шоколадные",
        brand = "Ваня-капитаня",
        ingredients = listOf(
            Ingredient("Шоколад", "Пальмовое масло", Risk.WARNING),
            Ingredient("Лесной орех", "Не содержит вредных добавок", Risk.SAFE)
        )
),
    Product(
        id = 2,
        name = "Чипсы",
        brand = "Добрый бульбаш",
        ingredients = listOf(
            Ingredient("Картофель", "Содержит крахмал", Risk.SAFE),
            Ingredient("Соль", "Соленая", Risk.WARNING),
            Ingredient("Консерванты", "Очень вредно", Risk.DANGER)
        )
    ),
    Product(
        id = 3,
        name = "Йогурт натуральный",
        brand = "Activia",
        ingredients = listOf(
            Ingredient("Молоко", "Содержит лактозу", Risk.DANGER),
            Ingredient("Закваска", "Полезные бактерии", Risk.SAFE)
        )
    )
)
val userAllergens = listOf("лактоза", "E102", "арахис", "глютен")
