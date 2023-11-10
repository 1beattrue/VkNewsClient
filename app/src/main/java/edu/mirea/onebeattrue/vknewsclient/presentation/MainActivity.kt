package edu.mirea.onebeattrue.vknewsclient.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import edu.mirea.onebeattrue.vknewsclient.ui.screens.MainScreen
import edu.mirea.onebeattrue.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val someState = remember {
                    mutableStateOf(true)
                }
                Log.d("MainActivity", "Recomposition: ${someState.value}")

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) { result ->
                    when (result) {
                        is VKAuthenticationResult.Success -> {
                            Log.d("MainActivity", "Success auth")
                        }

                        is VKAuthenticationResult.Failed -> {
                            Log.d("MainActivity", "Failed auth")
                        }
                    }
                }
                LaunchedEffect(key1 = someState.value) { // вызывается при первой рекомпозиции и при каждом изменении ключа
                    Log.d("MainActivity", "LaunchedEffect")
                }
                SideEffect {
                    Log.d("MainActivity", "SideEffect")
                    // launcher.launch(listOf(VKScope.WALL)) // код запустится после каждой успешной рекомпозиции основной функции
                }

                Button(onClick = { someState.value = !someState.value }) {
                    Text(text = "change state")
                }
                // MainScreen()
            }
        }
    }
}