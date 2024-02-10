package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.vknewsclient.data.repository.NewsFeedRepositoryImpl
import edu.mirea.onebeattrue.vknewsclient.domain.usecase.CheckAuthStateUseCase
import edu.mirea.onebeattrue.vknewsclient.domain.usecase.GetAuthStateFlowUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl(application)
    private val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)
    private val checkAuthStateUseCase = CheckAuthStateUseCase(repository)

    val authState = getAuthStateFlowUseCase()

    fun performAuthResult() {
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}