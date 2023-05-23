package com.example.listricks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.listricks.providers.UserProvider
import com.example.listricks.viewModels.UsersViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ListricksUsersViewModelTest {

    companion object {
        private const val VALID_USER_NAME: String = "Erez"
        private const val VALID_EMAIL: String = "Erez@gmail.com"
        private const val VALID_PASSWORD: String = "erez123"

        private const val SUCCESS: Boolean = true
        private const val FAILURE: Boolean = false
    }

    private lateinit var viewModel: UsersViewModel

    @get:Rule
    val instantLiveDate = InstantTaskExecutorRule()

    @Test
    fun signUpLiveData_defaultValue() = runTest {
        viewModel = UsersViewModel(FakeUserProvider(SUCCESS))
        Truth.assertThat(viewModel.signUpLiveData.value).isNull()
    }

    @Test
    fun loginLiveData_defaultValue() = runTest {
        viewModel = UsersViewModel(FakeUserProvider(SUCCESS))
        Truth.assertThat(viewModel.loginLiveData.value).isNull()
    }

    @Test
    fun signUpLiveData_signupFailure() = runTest {
        viewModel = UsersViewModel(FakeUserProvider(FAILURE))
        viewModel.signUpNewUser(VALID_USER_NAME, VALID_EMAIL, VALID_PASSWORD)
        Truth.assertThat(viewModel.signUpLiveData.value).isFalse()
    }

    @Test
    fun signUpLiveData_signupSuccess() = runTest {
        viewModel = UsersViewModel(FakeUserProvider(SUCCESS))
        viewModel.signUpNewUser(VALID_USER_NAME, VALID_EMAIL, VALID_PASSWORD)
        Truth.assertThat(viewModel.signUpLiveData.value).isTrue()
    }

    @Test
    fun loginLiveData_loginFailure() = runTest {
        viewModel = UsersViewModel(FakeUserProvider(FAILURE))
        viewModel.loginUser(VALID_USER_NAME, VALID_PASSWORD)
        Truth.assertThat(viewModel.loginLiveData.value).isFalse()
    }

    @Test
    fun loginLiveData_loginSuccess() = runTest {
        viewModel = UsersViewModel(FakeUserProvider(SUCCESS))
        viewModel.loginUser(VALID_USER_NAME, VALID_PASSWORD)
        Truth.assertThat(viewModel.loginLiveData.value).isTrue()
    }
}


class FakeUserProvider(private val returnValue: Boolean): UserProvider{
    override
    suspend fun createUser(username: String, email: String, password: String): Boolean {
        return returnValue
    }

    override
    suspend fun loginUser(username: String, password: String): Boolean {
        return returnValue
    }
}

