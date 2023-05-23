package com.example.listricks.providers

import com.example.listricks.data.AddListJson
import com.example.listricks.data.ListsApi
import com.example.listricks.data.ProductItemJson
import com.example.listricks.data.RetrofitHelper
import com.example.listricks.models.ListItem
import com.example.listricks.models.ProductItem
import retrofit2.Retrofit

class ListsProvider : ListsProviderInterface {
    private val instance: ListsApi by lazy {
        val retrofit: Retrofit = RetrofitHelper.createRetrofit()
        retrofit.create(ListsApi::class.java)
    }

    override suspend fun getLists(username: String): List<ListItem>? {
        try {
            val response = instance.getUserLists(username)
            val statusCode = response.code()
            if (statusCode != 200) {
                return emptyList()
            }
            return response.body()?.lists?.map { ListItem(it) }
        }
        catch (e: Exception) {
            return emptyList()
        }
    }

    override suspend fun addList(username: String, listName: String): List<ListItem>? {
        try {
            val addListJson = AddListJson(listName, emptyMap(), mutableListOf(username))
            val response = instance.addList(username, addListJson)
            val statusCode = response.code()
            if (statusCode != 200) {
                return emptyList()
            }
            return response.body()?.lists?.map { ListItem(it) }
        }
        catch (e: Exception) {
            return emptyList()
        }


    }

    override suspend fun deleteList(
        username: String,
        listName: String
    ): Pair<List<ListItem>?, Boolean> {
        return try {
            val response = instance.deleteList(username, listName)
            val statusCode = response.code()
            val list = response.body()?.lists?.map { ListItem(it) }
            Pair(list, statusCode == 200)
        } catch (e: Exception) {
            Pair(emptyList(), false)
        }
    }

    override suspend fun shareList(username: String, listName: String): Boolean {
        return try {
            val response = instance.shareList(listName, username)
            val statusCode = response.code()
            statusCode == 200
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getProducts(listName: String): List<ProductItem>? {
        try{
            val response = instance.getProducts(listName)
            val statusCode = response.code()
            if (statusCode != 200) {
                return emptyList()
            }
            return response.body()?.products?.map { ProductItem(it.key, it.value) }
        }
        catch (e: Exception) {
            return emptyList()
        }
    }

    override suspend fun addProduct(listName: String, productName: String): List<ProductItem>? {
        try {
            val productItemJson = ProductItemJson(productName, false)
            val response = instance.addProduct(listName, productItemJson)
            val statusCode = response.code()
            if (statusCode != 200) {
                return emptyList()
            }
            return response.body()?.products?.map { ProductItem(it.key, it.value) }
        }
        catch (e: Exception) {
            return emptyList()
        }
    }

    override suspend fun deleteProduct(
        listName: String,
        productName: String
    ): Pair<List<ProductItem>?, Boolean> {
        return try {
            val response = instance.deleteProduct(listName, productName)
            val statusCode = response.code()
            val list = response.body()?.products?.map { ProductItem(it.key, it.value) }
            Pair(list, statusCode == 200)
        } catch (e: Exception) {
            Pair(emptyList(), false)
        }
    }

    override suspend fun selectProduct(listName: String, productName: String) {
        try{
            instance.selectProduct(listName, productName)
        }
        catch (_: Exception) { }
    }
}