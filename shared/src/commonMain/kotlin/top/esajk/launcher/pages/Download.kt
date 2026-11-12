package top.esajk.launcher.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.get
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.IOException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import launcher.shared.generated.resources.Res
import launcher.shared.generated.resources.download
import top.esajk.launcher.AppState
import top.esajk.launcher.DrawerState
import top.esajk.launcher.LoadState
import kotlin.time.Instant

@Serializable
private enum class VersionType {
    @SerialName("snapshot")
    Snapshot,

    @SerialName("release")
    Release,

    @SerialName("old_beta")
    OldBeta,

    @SerialName("old_alpha")
    OldAlpha,
}

@Serializable
object Download

@Serializable
private data class VersionManifest(val latest: VersionManifestKeyLatest, val versions: List<Version>)

@Serializable
private data class VersionManifestKeyLatest(val release: String, val snapshot: String)

@Serializable
private data class Version(
    val id: String,
    val type: VersionType,
    val url: String,
    val time: Instant,
    val releaseTime: Instant,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadPage(drawerState: DrawerState) {
    drawerState.items = mapOf(
        "测试" to Pair(Res.drawable.download, Home),
    )

    val searchState = remember { TextFieldState() }
    var searchTypeState by remember { mutableStateOf("正式") }

    val types = mapOf(
        "正式" to VersionType.Release,
        "快照" to VersionType.Snapshot,
        "远古版测试" to VersionType.OldBeta,
        "远古阿尔法" to VersionType.OldAlpha,
    )

    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.width(5.dp))
            Text("名称")
            Spacer(Modifier.width(5.dp))
            TextField(
                state = searchState,
                label = { Text("搜索", color = MaterialTheme.colorScheme.onSurface) },
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = { Text("输入版本名称进行搜索", color = MaterialTheme.colorScheme.onSurface) },
            )

            Spacer(Modifier.width(5.dp))
            Text("版本类型")
            Spacer(Modifier.width(5.dp))

            // todo: 修改鼠标样式
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                TextField(
                    value = searchTypeState,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    types.forEach { (option, _) ->
                        DropdownMenuItem(
                            text = { Text(option, color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                searchTypeState = option
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }


        // todo: 缓存结果
        var manifest: LoadState<VersionManifest> by remember { mutableStateOf(LoadState.Loading) }
        var refresh by remember { mutableStateOf(false) }

        LaunchedEffect(searchState.text, searchTypeState, refresh) {
            manifest = LoadState.Loading

            val type = types[searchTypeState]!!

            try {
                val versionManifest: VersionManifest =
                    AppState.httpClient.get("https://piston-meta.mojang.com/mc/game/version_manifest.json").body()
                val versions = if (searchState.text.isNotEmpty()) {
                    versionManifest.versions.filter { it.type == type }.filter { it.id.contains(searchState.text) }
                } else {
                    versionManifest.versions.filter { it.type == type }
                }
                manifest = LoadState.Success(
                    versionManifest.copy(
                        versions = versions
                    )
                )
            } catch (err: IOException) {
                manifest = LoadState.Error(err.message ?: "发生错误: (没有详细信息)")
            } catch (err: SerializationException) {
                manifest = LoadState.Error(err.message!!)
            } catch (_: HttpRequestTimeoutException) {
                manifest = LoadState.Timeout
            }
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            when (val m = manifest) {
                is LoadState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoadState.Error -> {
                    Text("发生错误: ${m.message}", color = MaterialTheme.colorScheme.onSurface)
                }

                is LoadState.Success -> {
                    LazyColumn {
                        items(m.data.versions.size) { index ->
                            ListItem(
                                leadingContent = { },
                                headlineContent = { Text(m.data.versions[index].id) },
                                // todo: 时间本地化
                                supportingContent = { Text(m.data.versions[index].releaseTime.toString()) },
                            )
                        }
                    }
                }

                is LoadState.Timeout -> {
                    Text("请求超时", color = MaterialTheme.colorScheme.onSurface)
                }

            }

        }


    }
}

