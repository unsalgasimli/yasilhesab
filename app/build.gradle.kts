plugins {
    id("com.android.application") version "8.8.2"
    id("org.jetbrains.kotlin.android") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.jetbrains.kotlin.kapt") version "2.0.0"
    id("com.google.gms.google-services") version "4.4.2"
    id("com.google.dagger.hilt.android") version "2.56.2"
}

android {
    namespace = "com.unsalgasimliapplicationsnsug.yasilhesab"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.unsalgasimliapplicationsnsug.yasilhesab"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders += mapOf(
            "appAuthRedirectScheme" to "com.unsalgasimliapplicationsnsug.yasilhesab"
        )
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.05.00"))


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation ("com.airbnb.android:lottie-compose:6.3.0")
    implementation ("androidx.compose.foundation:foundation:1.6.0")
    implementation ("androidx.compose.animation:animation:1.6.0")


    implementation ("net.openid:appauth:0.11.1")


    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")


    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")


    implementation("com.google.dagger:hilt-android:2.56.2")
    kapt("com.google.dagger:hilt-compiler:2.56.2")


    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-functions-ktx")

    implementation("io.coil-kt:coil-compose:2.4.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.datastore:datastore-preferences:1.0.0")


    implementation("com.google.api-client:google-api-client-android:1.34.0")

    implementation("androidx.core:core-splashscreen:1.0.1")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
