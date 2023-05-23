package com.example.listricks.providers

import com.example.listricks.models.ListItem
import com.example.listricks.models.ProductItem

interface ListsProviderInterface {
    suspend fun getLists(username: String): List<ListItem>?

    suspend fun addList(username: String, listName: String): List<ListItem>?

    suspend fun deleteList(username: String, listName: String): Pair<List<ListItem>?, Boolean>

    suspend fun getProducts(listName: String): List<ProductItem>?

    suspend fun addProduct(listName: String, productName: String): List<ProductItem>?

    suspend fun deleteProduct(listName: String, productName: String): Pair<List<ProductItem>?, Boolean>

    suspend fun selectProduct(listName: String, productName: String)

    suspend fun shareList(username: String, listName: String): Boolean
}
