package edu.mirea.onebeattrue.vknewsclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.MainViewModel
import edu.mirea.onebeattrue.vknewsclient.presentation.screens.LoginScreen
import edu.mirea.onebeattrue.vknewsclient.presentation.screens.MainScreen
import edu.mirea.onebeattrue.vknewsclient.presentation.states.AuthState
import edu.mirea.onebeattrue.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) { result ->
                    viewModel.performAuthResult(result)
                }

                when (authState.value) {
                    AuthState.Authorized -> MainScreen()
                    AuthState.NotAuthorized -> LoginScreen {
                        launcher.launch(listOf(VKScope.WALL))
                    }
                    AuthState.Initial -> {}
                }
            }
        }
    }
}