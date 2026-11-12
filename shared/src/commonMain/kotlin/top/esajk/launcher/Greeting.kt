package top.esajk.launcher

import top.esajk.launcher.platform.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}