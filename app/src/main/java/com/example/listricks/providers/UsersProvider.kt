package com.example.listricks.providers

import com.example.listricks.data.AddUserJson
import com.example.listricks.data.RetrofitHelper
import com.example.listricks.data.UsersApi
import retrofit2.Retrofit

class UsersProvider : UserProvider {
    private val instance: UsersApi by lazy {
        val retrofit: Retrofit = RetrofitHelper.createRetrofit()
        retrofit.create(UsersApi::class.java)
    }

    override suspend fun createUser(username: String, email: String, password: String): Boolean {
        return try {
            val newUserJson = AddUserJson(username, email, password, emptyList())
            val response = instance.createUser(newUserJson)
            response.code() == 201
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun loginUser(username: String, password: String): Boolean {
        return try {
            val response = instance.loginUser(username, password)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
