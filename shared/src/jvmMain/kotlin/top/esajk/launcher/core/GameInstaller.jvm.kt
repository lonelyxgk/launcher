package top.esajk.launcher.core

import fr.flowarg.flowupdater.FlowUpdater
import fr.flowarg.flowupdater.versions.VanillaVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.esajk.launcher.platform.getUserHome
import java.nio.file.Files
import java.nio.file.Paths

actual suspend fun installMinecraft(versionId: String): String =
    withContext(Dispatchers.IO) {
        val gameDir = Paths.get(getUserHome(), "my-mc-launcher", versionId)
        Files.createDirectories(gameDir)

        val version = VanillaVersion.VanillaVersionBuilder()
            .withName(versionId)
            .build()

        val updater = FlowUpdater.FlowUpdaterBuilder()
            .withVanillaVersion(version)
            .build()

        updater.update(gameDir)
        gameDir.toAbsolutePath().toString()
    }
