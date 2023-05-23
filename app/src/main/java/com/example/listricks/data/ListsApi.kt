package com.example.listricks.data

import retrofit2.Response
import retrofit2.http.*

interface ListsApi {

    @GET("users/{username}/lists")
    suspend fun getUserLists(
        @Path("username") username: String
    ): Response<ListsListResponse>

    @PUT("users/{username}/lists")
    suspend fun addList(
        @Path("username") username: String,
        @Body list: AddListJson
    ): Response<ListsListResponse>

    @DELETE("users/{username}/lists/{listName}")
    suspend fun deleteList(
        @Path("username") username: String,
        @Path("listName") listName: String
    ): Response<ListsListResponse>

    @PUT("lists/{listName}/share")
    suspend fun shareList(
        @Path("listName") listName: String,
        @Body username: String
    ): Response<Void>


    @GET("lists/{listName}/products")
    suspend fun getProducts(
        @Path("listName") listName: String
    ): Response<ProductsListResponse>

    @PUT("lists/{listName}/products")
    suspend fun addProduct(
        @Path("listName") listName: String,
        @Body product: ProductItemJson
    ): Response<ProductsListResponse>

    @DELETE("lists/{listName}/products/{productName}")
    suspend fun deleteProduct(
        @Path("listName") listName: String,
        @Path("productName") productName: String
    ): Response<ProductsListResponse>

    @PUT("lists/{listName}/products/{productName}")
    suspend fun selectProduct(
        @Path("listName") listName: String,
        @Path("productName") productName: String
    ): Response<ProductsListResponse>
}