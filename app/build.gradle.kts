import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.VariantDimension
import de.rauschdo.eschistory.Configuration

plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.dagger.get().pluginId)
    id(libs.plugins.benchmark.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
}

inline fun <reified ValueT> VariantDimension.buildConfigField(
    name: String,
    value: ValueT
) {
    val resolvedValue = when (value) {
        is String -> "\"$value\"" // this function is mainly to improve this
        else -> value
    }.toString()
    buildConfigField(ValueT::class.java.simpleName, name, resolvedValue)
}

android {
    namespace = "de.rauschdo.eschistory"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        applicationId = "de.rauschdo.eschistory"
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk

        versionName = rootProject.extra["extraVersionName"] as String?
        versionCode = rootProject.extra["extraVersionCode"] as Int?

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        setProperty(
            "archivesBaseName",
            "esc_history_app_${versionName}_${versionCode}"
        )
    }

    kotlin {
        sourceSets.configureEach {
            kotlin.srcDir("${layout.buildDirectory.asFile.get()}/generated/ksp/$name/kotlin/")
        }
//        sourceSets.all {
//            languageSettings {
//                languageVersion = "2.0"
//            }
//        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }

        val release = getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // In real app, this would use its own release keystore
            signingConfig = signingConfigs.getByName("debug")
        }

        create("benchmark") {
            initWith(release)
            signingConfig = signingConfigs.getByName("debug")
            // Selects release buildType if the benchmark buildType not available in other modules.
            matchingFallbacks.add("release")
            proguardFiles("benchmark-rules.pro")
        }
    }

    // Future improvement (?); replace with jvmToolchain()
    compileOptions {
        sourceCompatibility = Configuration.javaVersion
        targetCompatibility = Configuration.javaVersion
    }
    kotlinOptions {
        jvmTarget = Configuration.kotlinJvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        // ignores private preview composables otherwise project showkase won"t let project compile
        arg("skipPrivatePreviews", "true")
    }

    testOptions.managedDevices {
        devices {
            maybeCreate<ManagedVirtualDevice>("pixel2api30").apply {
                device = "Pixel 2"
                apiLevel = 30
                systemImageSource = "google-atd"
            }
        }
        groups {
            maybeCreate("phoneAndTablet").apply {
                targetDevices.add(devices["pixel2api30"])
            }
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.ksp.api)
    //
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.material3)
    //
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    baselineProfile(project(":benchmark"))
    implementation(libs.androidx.hilt.navigation)
    //
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    ksp(libs.dagger.hilt.compiler)
    kspTest(libs.dagger.hilt.android.compiler)
    testAnnotationProcessor(libs.dagger.hilt.android.compiler)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
    androidTestAnnotationProcessor(libs.dagger.hilt.android.compiler)
    testImplementation(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    //
//    implementation(libs.retrofit)
//    implementation(libs.retrofit.gson)
//    implementation(libs.okhttp3.logging.interceptor)
//    implementation(libs.okhttp3.urlconnection)
    implementation(libs.coil.compose)
    implementation(libs.timber)
    debugImplementation(libs.showkase)
    kspDebug(libs.showkase.processor)
    debugImplementation(libs.leakCanary)
    //
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}
