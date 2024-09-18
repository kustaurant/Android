import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

val properties = Properties()
file("../local.properties").inputStream().use{ properties.load(it) }

android {
    namespace = "com.kust.kustaurant"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.kust.kustaurant"
        minSdk = 26
        targetSdk = 34
        versionCode = 10
        versionName = "0.1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 네이버 로그인
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${properties["naver_client_id"]}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${properties["naver_client_secret"]}\"")

        // 카카오 로그인
        buildConfigField("String", "KAKAO_NATIVE_KEY", "\"${properties["kakao_native_key"]}\"")
        buildConfigField("String", "KAKAO_REST_API_KEY", "\"${properties["kakao_rest_api_key"]}\"")

        manifestPlaceholders["KAKAO_NATIVE_KEY"] = properties["kakao_manifest_native_key"].toString()
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

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/NOTICE.md"
        }
    }
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    // sticky scollview
    implementation(libs.sticky.scroll.view)

    // Naver 지도
    implementation(libs.naver.maps)

    // by viewModels() 사용
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // ViewModelProvider 사용
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.oauth) // 네이버 로그인
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.v2.user) // 카카오 로그인
    kapt(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)

    implementation(libs.androidx.databinding.compiler)

    implementation(libs.google.material)

    implementation (libs.flexbox)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //MVVM Lifecycle JetPack 라이브러리
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation("androidx.activity:activity-ktx:1.5.0")

    implementation ("com.airbnb.android:lottie:6.5.2")


}
