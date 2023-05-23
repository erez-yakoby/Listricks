package com.example.listricks.models

import com.example.listricks.providers.ListsProviderInterface

data class ListsModel(private val _provider: ListsProviderInterface) {
    companion object {
        private lateinit var userName: String
        private lateinit var curList: String
        private var userLists: List<ListItem>? = null

        private const val LIST_NAME_SEPARATOR: String = "_"
    }

    fun setUserName(username: String) {
        userName = username
    }

    fun getUserName(): String {
        return userName
    }

    suspend fun getLists(): List<ListItem>? {
        if (userLists != null){
            return userLists
        }
        userLists = _provider.getLists(userName)
        return userLists
    }

    suspend fun addList(listName: String): Boolean {
        if (listName.isEmpty()) return false
        if (userLists != null && ListItem(listName) in userLists!!) return false

        val lists = _provider.addList(userName, userName + "_" + listName)!!
        if (lists.isNotEmpty()) {
            userLists = lists
            return true
        }
        return false
    }

    suspend fun deleteList(): Boolean {
        val response = _provider.deleteList(userName, curList)
        if (response.second){
            userLists = response.first
            return true
        }
        return false
    }

    suspend fun shareList(username: String): Boolean {
        return _provider.shareList(username, curList)
    }

    fun setCurList(listName: String) {
        curList = listName
    }

    fun getCurList(): String {
        return curList.split(LIST_NAME_SEPARATOR)[1]
    }

    suspend fun getProducts(): List<ProductItem>? {
        return _provider.getProducts(curList)
    }

    suspend fun addProduct(productName: String): List<ProductItem>? {
        return _provider.addProduct(curList, productName)
    }

    suspend fun deleteProduct(productName: String): Pair<List<ProductItem>?, Boolean>{
        return _provider.deleteProduct(curList, productName)

    }

    suspend fun selectProduct(productName: String) {
        _provider.selectProduct(curList, productName)
    }
}
