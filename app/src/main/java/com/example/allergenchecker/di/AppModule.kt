package com.example.allergenchecker.di

import androidx.room.Room
import com.example.allergenchecker.App
import com.example.allergenchecker.data.ProductRepository
import com.example.allergenchecker.data.db.AppDatabase
import com.example.allergenchecker.data.llm.LlmRepository
import com.example.allergenchecker.data.llm.LlmRepositoryImpl
import com.example.allergenchecker.domain.AnalyzeDietUseCase
import com.example.allergenchecker.ui.AddProductViewModel
import com.example.allergenchecker.ui.DetailViewModel
import com.example.allergenchecker.ui.HomeViewModel
import com.example.allergenchecker.ui.ProfileViewModel
import com.example.allergenchecker.ui.diet.DietAnalysisViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "allergen_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().userProfileDao() }
    single { ProductRepository(get(), get()) }

    single { (androidApplication() as App).realm }

    single<LlmRepository> {
        LlmRepositoryImpl(
            androidContext().contentResolver,
            CoroutineScope(Dispatchers.IO)
        )
    }
    single { AnalyzeDietUseCase(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { AddProductViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { DietAnalysisViewModel(androidApplication(), get(), get()) }
}