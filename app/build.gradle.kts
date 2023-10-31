plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.nyc_school"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nyc_school"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.4.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        buildConfig = true
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

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-graphics:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.material:material-icons-extended:1.6.0-alpha08")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.3")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.44.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-beta01")

    //SSJetPackComposeProgressButton
    implementation("com.github.SimformSolutionsPvtLtd:SSJetPackComposeProgressButton:1.0.7")

    //kotlin reflect
    implementation(kotlin("reflect"))

    //Flippable
    implementation("com.wajahatkarim:flippable:1.0.6")

    //Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Landscapist
    implementation("com.github.skydoves:landscapist-coil:2.2.8")
    implementation("com.github.skydoves:landscapist-placeholder:2.2.8")
    implementation("com.github.skydoves:landscapist-palette:2.2.8")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    //Paging Compose
    implementation("com.google.accompanist:accompanist-pager:0.13.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.13.0")

    //Permission
    implementation("com.google.accompanist:accompanist-permissions:0.25.0")

    //System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    //Preference
    implementation("androidx.preference:preference-ktx:1.2.1")

    //LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

    //Room
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")

    //test coroutine
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    //mockito
    testImplementation("org.mockito:mockito-core:3.12.4")
//    androidTestImplementation 'org.mockito:mockito-android:2.2.0'
    testImplementation("org.mockito:mockito-inline:3.11.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    //Custom crash activity
    implementation("cat.ereza:customactivityoncrash:2.4.0")
}