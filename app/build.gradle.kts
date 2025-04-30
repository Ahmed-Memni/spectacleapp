plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.spectacleapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.spectacleapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // 🧹 Enable ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 📦 Firebase BoM to manage Firebase versions automatically
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    // ✅ Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.firebase.firestore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("com.github.Dimezis:BlurView:version-2.0.3")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.android.material:material:1.6.0")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.zxing:core:3.3.0")
    implementation ("com.itextpdf:itext7-core:7.1.9")


}
