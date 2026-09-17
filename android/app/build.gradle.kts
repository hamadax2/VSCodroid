plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

fun directorySize(directory: File): Long {
    if (!directory.exists()) return 0L

    return directory.walkTopDown()
        .filter { it.isFile }
        .sumOf { it.length() }
}

fun largestFileSize(directory: File): Long {
    if (!directory.exists()) return 0L

    return directory.walkTopDown()
        .filter { it.isFile }
        .maxOfOrNull { it.length() } ?: 0L
}

android {
    namespace = "com.vscodroid"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.vscodroid"
        minSdk = 26
        targetSdk = 34

        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    // The lint baseline contains 3 issues recorded in lint-baseline.xml.
    /*
     * ------------------------------------------------------------
     * Build-time asset sizes
     * ------------------------------------------------------------
     *
     * The GitHub workflow prepares src/main/assets BEFORE Gradle
     * configuration starts.
     *
     * Therefore these values must be calculated here from the
     * actual files that exist in the current checkout/CI runner.
     */

    val assetsDir = file("src/main/assets")

    val extractedAssetBytes = directorySize(assetsDir)
    val largestAssetBytes = largestFileSize(assetsDir)

    val bundledUsrBytes =
        directorySize(file("src/main/assets/usr"))

    val bundledExtensionBytes =
        directorySize(file("src/main/assets/extensions"))

    val bundledServerBytes =
        directorySize(file("src/main/assets/vscode-reh"))

    buildTypes {
        debug {
            buildConfigField(
                "long",
                "EXTRACTED_ASSET_BYTES",
                "${extractedAssetBytes}L"
            )

            buildConfigField(
                "long",
                "LARGEST_ASSET_BYTES",
                "${largestAssetBytes}L"
            )

            buildConfigField(
                "long",
                "BUNDLED_USR_BYTES",
                "${bundledUsrBytes}L"
            )

            buildConfigField(
                "long",
                "BUNDLED_EXTENSION_BYTES",
                "${bundledExtensionBytes}L"
            )

            buildConfigField(
                "long",
                "BUNDLED_SERVER_BYTES",
                "${bundledServerBytes}L"
            )
        }

        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )

            buildConfigField(
                "long",
                "EXTRACTED_ASSET_BYTES",
                "${extractedAssetBytes}L"
            )

            buildConfigField(
                "long",
                "LARGEST_ASSET_BYTES",
                "${largestAssetBytes}L"
            )

            buildConfigField(
                "long",
                "BUNDLED_USR_BYTES",
                "${bundledUsrBytes}L"
            )

            buildConfigField(
                "long",
                "BUNDLED_EXTENSION_BYTES",
                "${bundledExtensionBytes}L"
            )

            buildConfigField(
                "long",
                "BUNDLED_SERVER_BYTES",
                "${bundledServerBytes}L"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.material)

    implementation(libs.play.asset.delivery)
    implementation(libs.play.asset.delivery.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit.jupiter.api)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.ui.test.junit4
    )

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
