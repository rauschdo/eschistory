@file:Suppress("UnstableApiUsage")

import de.rauschdo.eschistory.Configuration

plugins {
    id(libs.plugins.android.test.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.benchmark.get().pluginId)
}

android {
    namespace = "de.lottoth.online.benchmark"
    compileSdk = Configuration.compileSdk

    compileOptions {
        sourceCompatibility = Configuration.javaVersion
        targetCompatibility = Configuration.javaVersion
    }

    kotlinOptions {
        jvmTarget = Configuration.kotlinJvmTarget
    }

    defaultConfig {
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    flavorDimensions += "server"
    productFlavors {
        create("envLts") {
            dimension = "server"
        }
        create("envDev") {
            dimension = "server"
        }
        create("envTest") {
            dimension = "server"
        }
        create("envProd") {
            dimension = "server"
        }
    }

    testOptions.managedDevices.devices {
        maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6api31").apply {
            device = "Pixel 6"
            apiLevel = 31
            systemImageSource = "aosp"
        }
    }
}

// This is the plugin configuration. Everything is optional. Defaults are in the
// comments. In this example, you use the GMD added earlier and disable connected devices.
baselineProfile {

    // This specifies the managed devices to use that you run the tests on. The default
    // is none.
    managedDevices += "pixel6api31"

    // This enables using connected devices to generate profiles. The default is true.
    // When using connected devices, they must be rooted or API 33 and higher.
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro)
}
