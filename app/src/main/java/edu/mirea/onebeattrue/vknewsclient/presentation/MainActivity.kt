package edu.mirea.onebeattrue.vknewsclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.MainViewModel
import edu.mirea.onebeattrue.vknewsclient.presentation.screens.LoginScreen
import edu.mirea.onebeattrue.vknewsclient.presentation.screens.MainScreen
import edu.mirea.onebeattrue.vknewsclient.domain.entity.AuthState
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.MainViewModelFactory
import edu.mirea.onebeattrue.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(application)
                )
                val authState = viewModel.authState.collectAsState(AuthState.Initial)

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    viewModel.performAuthResult()
                }

                when (authState.value) {
                    AuthState.Authorized -> MainScreen()
                    AuthState.NotAuthorized -> LoginScreen {
                        launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                    }

                    AuthState.Initial -> {}
                }
            }
        }
    }
}