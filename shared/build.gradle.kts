plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(ktorLibs.plugins.ktor)
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.navigation)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)
            implementation(ktorLibs.client.core)
            implementation(ktorLibs.client.cio)
            implementation(ktorLibs.client.contentNegotiation)
            implementation(ktorLibs.serialization.kotlinx.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
        }
        jvmMain.dependencies {
        }
        jvmTest.dependencies {
        }
    }
}