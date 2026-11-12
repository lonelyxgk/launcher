package top.esajk.launcher.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun HomePage() {
    Text("Home")
}