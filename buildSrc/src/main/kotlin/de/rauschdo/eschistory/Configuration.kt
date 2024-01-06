package de.rauschdo.eschistory

import org.gradle.api.JavaVersion

object Configuration {
  const val compileSdk = 34
  const val targetSdk = 34
  const val minSdk = 26 // Android 8
  val javaVersion = JavaVersion.VERSION_17
  // Check to place with correct string corresponding to [javaVersion] above
  // AGBP should fail anyway if does not align
  const val kotlinJvmTarget = "17"
}
