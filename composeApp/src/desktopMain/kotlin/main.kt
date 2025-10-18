import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.miuix.countdown.App
import com.miuix.countdown.viewmodel.CountdownViewModel

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "倒计时",
    ) {
        // Note: Desktop version would need a different repository implementation
        // This is a placeholder
    }
}

