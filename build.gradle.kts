buildscript {
    dependencies {
        classpath("io.github.xilinjia.krdb:gradle-plugin:3.2.8")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}