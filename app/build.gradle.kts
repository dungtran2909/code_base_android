import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

fun getProps(fileName: String): Properties {
    val props = Properties()
    val propFile = rootProject.file(fileName) // Lấy file từ root project
    if (propFile.exists()) {
        props.load(FileInputStream(propFile))
    }
    return props
}

fun Properties.getSafe(key: String): String {
    val value = getProperty(key) ?: ""
    return "\"$value\""
}

android {
    namespace = "com.dungtran.codebase"
    compileSdk = libs.versions.project.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dungtran.codebase"
        minSdk = libs.versions.project.minSdk.get().toInt()
        targetSdk = libs.versions.project.targetSdk.get().toInt()
        versionCode = libs.versions.project.versionCode.get().toInt()
        versionName = libs.versions.project.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "environment"
    productFlavors {
        // 2. Môi trường Development
        create("develop") {
            dimension = "environment"
            applicationIdSuffix = ".dev" // Gói app sẽ là com.dungtran.codebase.dev
            versionNameSuffix = "-dev"
            
            // Bạn có thể định nghĩa Base URL cho API tại đây
            val devProps = getProps("develop.properties")
            buildConfigField("String", "BASE_URL", devProps.getSafe("BASE_URL"))
            buildConfigField("String", "API_KEY", devProps.getSafe("API_KEY"))
        }

        // 3. Môi trường Production
        create("product") {
            dimension = "environment"
            // Giữ nguyên applicationId gốc: com.dungtran.codebase
            
            val prodProps = getProps("product.properties")
            buildConfigField("String", "BASE_URL", prodProps.getSafe("BASE_URL"))
            buildConfigField("String", "API_KEY", prodProps.getSafe("API_KEY"))
        }
    }


    buildTypes {
        getByName("debug") {
            // Cấu hình cho bản Debug của cả 2 môi trường
            isMinifyEnabled = false
        }
        release {
            // Cấu hình cho bản Release của cả 2 môi trường
            isMinifyEnabled = true // Nên bật để tối ưu app
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.com.google.android.material.material)
    
    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Firebase BOM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    // Another lib support by Kotlinx
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.play.services)

    // DI (Hilt)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    // Remote (Retrofit + Moshi)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging)

    // Local (Room)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Background (WorkManager + Hilt Worker)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}