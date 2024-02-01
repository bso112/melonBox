import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.common.base.Charsets
import java.util.Properties
import java.io.InputStreamReader
import java.io.FileInputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlinx.serialization)
}


fun gradleProperties(propertiesFilePath: String): Properties {
    val properties = Properties()
    val localProperties = File(rootDir, propertiesFilePath)

    if (localProperties.isFile) {
        InputStreamReader(
            FileInputStream(localProperties),
            Charsets.UTF_8
        ).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}


val googleOauthClientId: String =
    gradleProperties("apikey.properties").getProperty("GOOGLE_OAUTH_CLIENT_ID")
val googleOauthClientSecret: String =
    gradleProperties("apikey.properties").getProperty("GOOGLE_OAUTH_CLIENT_SECRET")
val googleApiKey: String =
    gradleProperties("apikey.properties").getProperty("GOOGLE_API_KEY")

android {
    namespace = "com.seoulventure.melonbox"
    compileSdk = 34

    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.seoulventure.melonbox"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GOOGLE_OAUTH_CLIENT_ID", googleOauthClientId)
        buildConfigField("String", "GOOGLE_OAUTH_CLIENT_SECRET", googleOauthClientSecret)
        buildConfigField("String", "GOOGLE_API_KEY", googleApiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    implementation(libs.bundles.ktor)

    implementation(platform("com.google.firebase:firebase-bom:30.2.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation(libs.play.services.auth)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}