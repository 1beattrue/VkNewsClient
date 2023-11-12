package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import edu.mirea.onebeattrue.vknewsclient.ui.states.AuthState

class MainViewModel : ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState>
        get() = _authState

    init {
        _authState.value = if (VK.isLoggedIn()) AuthState.Authorized else AuthState.NotAuthorized
    }

    fun performAuthResult(result: VKAuthenticationResult) {
        _authState.value = if (result is VKAuthenticationResult.Success) {
            AuthState.Authorized
        } else {
            Log.d(
                "MainViewModel",
                (result as VKAuthenticationResult.Failed).exception.message.toString()
            )
            AuthState.NotAuthorized
        }
    }
}