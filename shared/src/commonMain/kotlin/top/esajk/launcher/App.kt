package top.esajk.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource

import launcher.shared.generated.resources.Res
import launcher.shared.generated.resources.app_name
import launcher.shared.generated.resources.arrow_back
import launcher.shared.generated.resources.close
import launcher.shared.generated.resources.download
import launcher.shared.generated.resources.minimize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import top.esajk.launcher.pages.Download
import top.esajk.launcher.pages.DownloadPage
import top.esajk.launcher.pages.Home
import top.esajk.launcher.pages.HomePage

@Composable
fun TitleBar(state: WindowState, onCloseRequest: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(38.dp).background(Color.Cyan),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(Res.string.app_name),
        )

        Spacer(modifier = Modifier.weight(1f))

        WindowControls(state, onCloseRequest)
    }
}

@Composable
fun WindowControls(state: WindowState, onClose: () -> Unit) {
    IconButton(onClick = { state.isMinimized = true }, shape = CircleShape) {
        Icon(
            painter = painterResource(Res.drawable.minimize),
            contentDescription = "Minimize"
        )
    }
    IconButton(onClick = onClose, shape = CircleShape) {
        Icon(
            painter = painterResource(Res.drawable.close),
            contentDescription = "Close"
        )
    }
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavigationDrawer()
    }
}

class DrawerState(
    map: Map<String, Pair<DrawableResource, Any>>,
) {
    var items by mutableStateOf(map)
    var selectedItem: Int? by mutableStateOf(null)
}

@Composable
fun NavigationDrawer() {
    val navController = rememberNavController()

    val menus = mapOf(
        stringResource(Res.string.download) to Pair(Res.drawable.download, Download),
    )
    val state = DrawerState(
        menus
    )
    val drawerState = remember {
        state
    }

    var isHome by remember { mutableStateOf(true) }

    PermanentNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(180.dp),
                drawerContainerColor = Color.LightGray,
            ) {
                Spacer(Modifier.height(18.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (isHome) {
                        // todo: 改为显示账号信息
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(Res.string.app_name),
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                navController.navigate(Home) {
                                    popUpTo(navController.graph.id) { inclusive = true }
                                }
                                drawerState.items = menus
                                drawerState.selectedItem = null
                                isHome = true
                            },
                            shape = CircleShape
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.arrow_back),
                                contentDescription = "Close"
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                drawerState.items.entries.forEachIndexed { index, (name, value) ->
                    val (icon, navTo) = value

                    NavigationDrawerItem(
                        label = { Text(name) },
                        icon = { Icon(painter = painterResource(icon), contentDescription = name) },
                        selected = index == drawerState.selectedItem,
                        onClick = {
                            navController.navigate(navTo) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                            isHome = false
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Box(Modifier.fillMaxSize().background(Color.White)) {
                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> { HomePage() }
                    composable<Download> { DownloadPage(drawerState) }
                }
            }
        }
    )
}
