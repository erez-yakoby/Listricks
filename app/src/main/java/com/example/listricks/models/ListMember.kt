package com.example.listricks.models

interface ListMember

data class ListItem(val listName: String) : ListMember {
    override fun equals(other: Any?): Boolean {
        if (other !is ListItem) return false
        return this.listName == other.listName
    }

    override fun hashCode(): Int {
        return listName.hashCode()
    }
}

data class ProductItem(val productName: String, var isMarked: Boolean) : ListMember {
    override fun equals(other: Any?): Boolean {
        if (other !is ProductItem) return false
        return this.productName == other.productName
    }

    override fun hashCode(): Int {
        return this.productName.hashCode()
    }
}
