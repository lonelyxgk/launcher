package top.esajk.launcher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main(): Unit = application {
    val state = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        transparent = true,
        undecorated = true,
        state = state,
        icon = null,
        title = "launcher",
    ) {
        val density = LocalDensity.current
        window.minimumSize = with(density) {
            Dimension(800.dp.roundToPx(), 500.dp.roundToPx())
        }

        Surface(
            color = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                TitleBar(state, ::exitApplication)
                WindowDraggableArea {
                    App()
                }
            }
        }
    }
}
