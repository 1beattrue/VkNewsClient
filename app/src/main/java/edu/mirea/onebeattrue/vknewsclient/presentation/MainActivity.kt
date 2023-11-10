package edu.mirea.onebeattrue.vknewsclient.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.NewsFeedViewModel
import edu.mirea.onebeattrue.vknewsclient.ui.screens.MainScreen
import edu.mirea.onebeattrue.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
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
                launcher.launch(listOf(VKScope.WALL))
                MainScreen()
            }
        }
    }
}