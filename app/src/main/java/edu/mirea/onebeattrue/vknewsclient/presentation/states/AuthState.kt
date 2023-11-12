package edu.mirea.onebeattrue.vknewsclient.presentation.states

sealed class AuthState {
    object Initial: AuthState()
    object Authorized: AuthState()
    object NotAuthorized: AuthState()
}