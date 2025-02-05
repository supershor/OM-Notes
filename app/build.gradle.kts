plugins {
    alias(libs.plugins.android.application)
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\ashis\\OneDrive\\Documents\\Android keys\\OM_Writes.jks")
            storePassword = "ashish72240"
            keyAlias = "key0"
            keyPassword = "ashish72240"
        }
    }
    namespace = "com.om_tat_sat.OM_Notes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.om_tat_sat.OM_Notes"
        minSdk = 24
        targetSdk = 34
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
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //noinspection UseTomlInstead
    implementation ("com.intuit.ssp:ssp-android:1.1.1")
    //noinspection UseTomlInstead
    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    //noinspection UseTomlInstead
    implementation ("com.airbnb.android:lottie:6.2.0")


    implementation (libs.play.services.auth)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}