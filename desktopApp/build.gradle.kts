import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "top.esajk.launcher.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm, TargetFormat.Exe)
            packageName = "top.esajk.launcher"
            packageVersion = "1.0.0"
            description = "a minecraft launcher"
            // todo: gpl
            copyright = ""
            // licenseFile.set(project.file("LICENSE.txt"))
            
            // macOS {
            //     iconFile.set(project.file("icon.icns"))
            // }
            // windows {
            //     dirChooser = true
            //     iconFile.set(project.file("icon.ico"))
            // }
            // linux {
            //     debMaintainer = "maintainer@example.com"
            //     iconFile.set(project.file("icon.png"))
            // }
        }
    }
}