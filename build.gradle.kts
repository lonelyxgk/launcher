plugins {
    // 这样做是为了避免插件在各个子项目的类加载器（classloader）中被多次加载
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(ktorLibs.plugins.ktor) apply false
}