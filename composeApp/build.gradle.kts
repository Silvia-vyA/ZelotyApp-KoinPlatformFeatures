import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("app.cash.sqldelight")
    kotlin("multiplatform")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}
kotlin {

    sourceSets {

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("io.mockk:mockk:1.13.9")
            implementation("app.cash.turbine:turbine:1.0.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        }

        androidInstrumentedTest.dependencies {
            implementation("androidx.compose.ui:ui-test-junit4")
            implementation("io.mockk:mockk-android:1.13.9")
            implementation("junit:junit:4.13.2")
        }
    }
}

android {
    dependencies {

        debugImplementation(
            "androidx.compose.ui:ui-test-manifest"
        )

    }
}
kotlin {
    // Pastikan androidTarget dipanggil setelah buildFeatures
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)  // Set JVM Target untuk Kotlin ke JVM 11
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation("app.cash.sqldelight:runtime:2.0.1")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
                implementation("com.russhwolf:multiplatform-settings:1.1.1")
                implementation("io.insert-koin:koin-core:3.5.3")
                implementation("io.insert-koin:koin-compose:1.1.2")
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
            }
        }

        androidMain {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation("androidx.navigation:navigation-compose:2.7.7")
                implementation("app.cash.sqldelight:android-driver:2.0.1")
                implementation("io.insert-koin:koin-android:3.5.3")
                implementation("io.insert-koin:koin-androidx-compose:3.5.3")

                // Ktor dependencies
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
            }
        }
    }

    // Memastikan androidTarget dipanggil di akhir
    androidTarget()  // Pastikan dipanggil setelah buildFeatures
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Membaca local.properties untuk GEMINI_API_KEY
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use {
                localProperties.load(it)
            }
        }

        // Menambahkan API Key sebagai field dalam BuildConfig
        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${localProperties.getProperty("GEMINI_API_KEY", "")}\""
        )
    }

    // Pastikan buildFeatures ada di dalam blok android
    buildFeatures {
        buildConfig = true  // Aktifkan BuildConfig untuk menggunakan API Key
    }

    // Set JVM Compatibility untuk Java ke JVM 11
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("NotesDatabase") {
            packageName.set("org.example.project.db")
        }
    }
}