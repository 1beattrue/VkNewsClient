package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import edu.mirea.onebeattrue.vknewsclient.presentation.states.AuthState

class MainViewModel(
    application: Application
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState>
        get() = _authState

    init {
        val storage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(storage)
        val loggedIn = token != null && token.isValid

        _authState.value = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
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