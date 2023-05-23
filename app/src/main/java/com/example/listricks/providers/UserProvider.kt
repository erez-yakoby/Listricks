package com.example.listricks.providers

interface UserProvider {

    suspend fun createUser(username: String, email: String, password: String): Boolean
    suspend fun loginUser(username: String, password: String): Boolean


}