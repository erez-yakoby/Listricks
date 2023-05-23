package com.example.listricks.models

import com.example.listricks.providers.UserProvider

data class UsersModel(private val usersProvider: UserProvider) {

    companion object {
        private const val MAX_PASSWORD_LENGTH: Int = 12
    }

    suspend fun signUpUser(username: String, email: String, password: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) return false
        if (password.length > MAX_PASSWORD_LENGTH) return false
        return usersProvider.createUser(username, email, password)
    }

    suspend fun loginUser(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) return false
        return usersProvider.loginUser(username, password)
    }
}
