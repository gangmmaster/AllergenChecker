package com.example.allergenchecker

import android.app.Application
import com.example.allergenchecker.data.realm.Additive
import com.example.allergenchecker.data.realm.AdditiveSeeder
import com.example.allergenchecker.di.appModule
import io.github.xilinjia.krdb.Realm
import io.github.xilinjia.krdb.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File
import java.io.FileOutputStream

class App : Application() {

    lateinit var realm: Realm
        private set

    override fun onCreate() {
        super.onCreate()

        realm = Realm.open(
            RealmConfiguration.Builder(schema = setOf(Additive::class))
                .build()
        )
        AdditiveSeeder.seedIfEmpty(realm)

        copyModelFromAssets()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun copyModelFromAssets() {
        val modelName = "qwen2.5-1.5b-instruct-q4_k_m.gguf"
        val destFile = File(filesDir, modelName)

        if (!destFile.exists()) {
            try {
                val inputStream = assets.open(modelName)
                val outputStream = FileOutputStream(destFile)
                val buffer = ByteArray(1024 * 1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}