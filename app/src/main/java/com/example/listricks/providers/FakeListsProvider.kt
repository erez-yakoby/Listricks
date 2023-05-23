package com.example.listricks.providers

import com.example.listricks.models.ListItem
import com.example.listricks.models.ProductItem


class FakeListsProvider : ListsProviderInterface {

    private var db = hashMapOf<ListItem, MutableMap<String, ProductItem>>()

    override suspend fun getLists(username: String): List<ListItem> {
        return db.keys.toList()
    }

    override suspend fun addList(username: String, listName: String): List<ListItem> {
        db[ListItem(listName)] = mutableMapOf()
        return db.keys.toList()
    }

    override suspend fun deleteList(
        username: String,
        listName: String
    ): Pair<List<ListItem>?, Boolean> {
        db.remove(ListItem(listName))
        return Pair(db.keys.toList(), true)
    }

    override suspend fun getProducts(listName: String): List<ProductItem> {
        return db[ListItem(listName)]!!.values.toList()
    }

    override suspend fun addProduct(listName: String, productName: String): List<ProductItem> {
        db[ListItem(listName)]!![productName] = ProductItem(productName, false)
        return db[ListItem(listName)]!!.values.toList()
    }

    override suspend fun deleteProduct(
        listName: String,
        productName: String
    ): Pair<List<ProductItem>?, Boolean> {
        db[ListItem(listName)]!!.remove(productName)
        return Pair(db[ListItem(listName)]!!.values.toList(), true)
    }

    override suspend fun selectProduct(listName: String, productName: String) {
        db[ListItem(listName)]!![productName]!!.isMarked =
            !db[ListItem(listName)]!![productName]!!.isMarked
    }

    override suspend fun shareList(username: String, listName: String): Boolean {
        return true
    }
}