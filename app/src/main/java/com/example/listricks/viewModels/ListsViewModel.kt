package com.example.listricks.viewModels

import androidx.lifecycle.*
import com.example.listricks.models.ListItem
import com.example.listricks.models.ListsModel
import com.example.listricks.models.ProductItem
import com.example.listricks.providers.ListsProviderInterface
import kotlinx.coroutines.launch

class ListsViewModel(_provider: ListsProviderInterface) : ViewModel() {

    private val model = ListsModel(_provider)

    private val _listsLiveData = MutableLiveData<List<ListItem>>()
    val listsLiveData: LiveData<List<ListItem>> = _listsLiveData

    private val _productsLiveData = MutableLiveData<List<ProductItem>>()
    val productsLiveData: LiveData<List<ProductItem>> = _productsLiveData

    private val _shareListLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val shareListLiveData: LiveData<Boolean> = _shareListLiveData

    private val _addListLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val addListLiveData: LiveData<Boolean> = _addListLiveData

    private val _deleteListLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val deleteListLiveData: LiveData<Boolean> = _deleteListLiveData

    fun setUserName(userName: String) {
        viewModelScope.launch {
            model.setUserName(userName)
        }
    }

    fun getUserName(): String {
        return model.getUserName()
    }

    fun loadLists() {
        viewModelScope.launch {
            _listsLiveData.postValue(model.getLists())
        }
    }

    fun addList(listName: String) {
        viewModelScope.launch {
            _addListLiveData.postValue(model.addList(listName))
        }
    }

    fun deleteList() {
        viewModelScope.launch {
            _deleteListLiveData.postValue(model.deleteList())
        }
    }

    fun setCurList(listName: String) {
        model.setCurList(listName)
    }

    fun getCurList(): String {
        return model.getCurList()
    }


    fun loadProducts() {
        viewModelScope.launch {
            _productsLiveData.postValue(model.getProducts())
        }
    }

    fun addProduct(productName: String) {
        viewModelScope.launch {
            val list = model.addProduct(productName)!!
            if (list.isNotEmpty()) {
                _productsLiveData.postValue(list)
            }
        }
    }

    fun deleteProduct(productName: String) {
        viewModelScope.launch {
            val response = model.deleteProduct(productName)
            if (response.second) {
                _productsLiveData.postValue(response.first!!)
            }
        }
    }

    fun selectProduct(productName: String) {
        viewModelScope.launch {
            model.selectProduct(productName)
        }
    }

    fun shareList(username: String) {
        viewModelScope.launch {
            _shareListLiveData.postValue(model.shareList(username))
        }
    }

    class Factory(
        private val listsProviderFactory: () -> ListsProviderInterface
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ListsViewModel(listsProviderFactory()) as T
        }
    }
}