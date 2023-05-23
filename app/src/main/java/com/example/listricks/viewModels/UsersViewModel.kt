package com.example.listricks.viewModels

import androidx.lifecycle.*
import com.example.listricks.models.UsersModel
import com.example.listricks.providers.UserProvider
import kotlinx.coroutines.launch

class UsersViewModel(userProvider: UserProvider) : ViewModel() {
    private val usersModel = UsersModel(userProvider)

    private val _loginLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)
    val loginLiveData: LiveData<Boolean?> = _loginLiveData

    private val _signUpLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)
    val signUpLiveData: LiveData<Boolean?> = _signUpLiveData


    fun signUpNewUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpLiveData.postValue(usersModel.signUpUser(username, email, password))
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _loginLiveData.postValue(usersModel.loginUser(username, password))
        }
    }

    fun resetLiveData() {
        viewModelScope.launch {
            _signUpLiveData.postValue(null)
        }
        viewModelScope.launch {
            _loginLiveData.postValue(null)
        }
    }

    class Factory(
        private val usersProviderFactory: () -> UserProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(usersProviderFactory()) as T
        }
    }
}