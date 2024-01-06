plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger) apply false
    alias(libs.plugins.benchmark) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    // App versionname and -code
    val versionName = "1.0.0_2024"
    val versionCode = 1
    // Extras declared (only have these like this to serve as sample)
    val extraVersionName by extra(versionName)
    val extraVersionCode by extra(versionCode)
}