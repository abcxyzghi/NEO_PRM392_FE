plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.electronics_store"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.electronics_store"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

dependencies {
    implementation(libs.converter.gson)
    implementation(libs.glide)
    implementation(libs.jwtdecode)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout.v214)
    implementation(libs.play.services.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.appcompat)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.messaging)
    implementation(libs.play.services.measurement)
    implementation(libs.play.services.measurement.api)
    implementation(libs.play.services.measurement.base)
    implementation(libs.play.services.measurement.impl)
    implementation(libs.play.services.measurement.sdk)
    implementation(libs.play.services.measurement.sdk.api)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.circleimageview)
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation ("com.google.android.gms:play-services-maps:18.1.0")

    implementation(libs.okhttp)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.fido)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}