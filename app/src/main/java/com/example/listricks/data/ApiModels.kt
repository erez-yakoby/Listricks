package com.example.listricks.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddUserJson(
    @Json(name = "userId")
    val userId: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "listsId")
    val listsId: List<String>,
)

@JsonClass(generateAdapter = true)
data class AddListJson(
    @Json(name = "listId")
    val listId: String,
    @Json(name = "products")
    val products: Map<String, Boolean>,
    @Json(name = "usersAccess")
    val usersAccess: List<String>,
)

@JsonClass(generateAdapter = true)
data class ProductItemJson(
    @Json(name = "productName")
    val productName: String,
    @Json(name = "isMarked")
    val isMarked: Boolean,
)

@JsonClass(generateAdapter = true)
data class ListsListResponse(
    @Json(name = "lists")
    val lists: List<String>
)

@JsonClass(generateAdapter = true)
data class ProductsListResponse(
    @Json(name = "products")
    val products: Map<String, Boolean>
)




