package edu.mirea.onebeattrue.vknewsclient.ui.states

sealed class AuthState {
    object Initial: AuthState()
    object Authorized: AuthState()
    object NotAuthorized: AuthState()
}