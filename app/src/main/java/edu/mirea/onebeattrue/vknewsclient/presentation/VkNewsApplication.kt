package edu.mirea.onebeattrue.vknewsclient.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import edu.mirea.onebeattrue.vknewsclient.di.ApplicationComponent
import edu.mirea.onebeattrue.vknewsclient.di.DaggerApplicationComponent

class VkNewsApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("ApplicationComponent", "getApplicationComponent")
    return (LocalContext.current.applicationContext as VkNewsApplication).component
}