plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.vahan.dartagnan"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vahan.dartagnan"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests.all {
            it.testLogging {
                showStandardStreams = true
                events("passed", "failed", "skipped")
            }
        }
    }
}

// AJOUT DE LA TÂCHE DE LANCEMENT DU PONT ROYAL (V4.2 - Ultra Robust)
// On utilise une approche qui ne dépend pas des SourceSets standards de Java
tasks.register<JavaExec>("runBridge") {
    group = "dartagnan"
    description = "Lance le Pont Royal pour la page Web"
    
    // On repasse en fun main()
    mainClass.set("com.vahan.dartagnan.DartagnanBridgeKt")
    
    val compileKotlin = tasks.getByPath(":app:compileDebugKotlin") as org.jetbrains.kotlin.gradle.tasks.KotlinCompile
    val runtimeConfig = configurations.getByName("debugRuntimeClasspath")

    classpath = files(compileKotlin.destinationDirectory) + runtimeConfig
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation("io.ktor:ktor-server-core-jvm:3.1.1")
    implementation("io.ktor:ktor-server-netty-jvm:3.1.1")
    implementation("io.ktor:ktor-server-cors-jvm:3.1.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.1.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.1.1")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
