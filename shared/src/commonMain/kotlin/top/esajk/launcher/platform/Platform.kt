package top.esajk.launcher.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getUserHome(): String