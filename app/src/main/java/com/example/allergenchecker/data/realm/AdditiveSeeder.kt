package com.example.allergenchecker.data.realm

import io.github.xilinjia.krdb.Realm
import io.github.xilinjia.krdb.ext.query

object AdditiveSeeder {

    fun seedIfEmpty(realm: Realm) {
        val count = realm.query(Additive::class).count().find()
        if (count > 0) return

        realm.writeBlocking {
            copyToRealm(Additive().apply {
                id = 1
                marker = "E102"
                description = "Тартразин — синтетический краситель"
                riskLevel = "DANGER"
            })
            copyToRealm(Additive().apply {
                id = 2
                marker = "E322"
                description = "Лецитин — эмульгатор"
                riskLevel = "WARNING"
            })
            copyToRealm(Additive().apply {
                id = 3
                marker = "E621"
                description = "Глутамат натрия — усилитель вкуса"
                riskLevel = "WARNING"
            })
            copyToRealm(Additive().apply {
                id = 4
                marker = "e250"
                description = "Нитрит натрия — консервант"
                riskLevel = "DANGER"
            })

            copyToRealm(Additive().apply {
                id = 5
                marker = "water"
                description = "Вода"
                riskLevel = "SAFE"
            })
            copyToRealm(Additive().apply {
                id = 6
                marker = "fructose-glucose syrup"
                description = "Глюкозно-фруктозный сироп — подсластитель"
                riskLevel = "WARNING"
            })
            copyToRealm(Additive().apply {
                id = 7
                marker = "carbon dioxide"
                description = "Углекислый газ — создает пузырьки"
                riskLevel = "SAFE"
            })
            copyToRealm(Additive().apply {
                id = 8
                marker = "e150d"
                description = "Карамельный колер — краситель"
                riskLevel = "WARNING"
            })
            copyToRealm(Additive().apply {
                id = 9
                marker = "phosphoric acid"
                description = "Ортофосфорная кислота — регулятор кислотности"
                riskLevel = "WARNING"
            })
            copyToRealm(Additive().apply {
                id = 10
                marker = "natural flavorings"
                description = "Натуральные ароматизаторы (состав не раскрыт)"
                riskLevel = "SAFE"
            })
        }
    }
}