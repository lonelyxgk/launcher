package top.esajk.launcher.core

/**
 * 下载并安装指定版本的 Minecraft，返回游戏目录。
 */
expect suspend fun installMinecraft(versionId: String): String
