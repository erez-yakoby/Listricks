package com.example.listricks.data

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.*


interface UsersApi {
    @POST("users")
    suspend fun createUser(
        @Body user: AddUserJson
    ): Response<Void>

    @GET("users/login")
    suspend fun loginUser(
        @Query("username") userName: String,
        @Query("password") password: String,
    ): Response<Void>
}