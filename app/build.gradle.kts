plugins {
    id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.planetzeapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planetzeapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true // To enable MultiDex and use its advantage of using 65,536 plus method references

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Firebase SDKs
    implementation("com.google.firebase:firebase-auth")       // Firebase Authentication
    implementation("com.google.firebase:firebase-database")   // Firebase Realtime Database
    implementation("com.google.firebase:firebase-analytics") // Firebase Analytics

    // For Card view
    implementation("androidx.cardview:cardview:1.0.0") // Card View Type Layout in XML

    // For PieChart in Results Display
    implementation("com.github.AnyChart:AnyChart-Android:1.1.2") // GitHub AnyChart Repository
    implementation("androidx.multidex:multidex:2.0.1") // For AnyChart Usage for use of 65,536 plus method references

    testImplementation("junit:junit:4.+")
    testImplementation("org.mockito:mockito-inline:3.12.4")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    testImplementation("net.bytebuddy:byte-buddy:1.14.8")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.14.8")
}