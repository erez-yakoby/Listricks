package com.example.listricks

import com.example.listricks.models.UsersModel
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ListricksUsersModelTest {
    companion object {
        private const val EMPTY_STRING: String = ""

        private const val VALID_USER_NAME: String = "Erez"
        private const val VALID_EMAIL: String = "Erez@gmail.com"
        private const val VALID_PASSWORD: String = "erez123"

        private const val INVALID_LONG_PASSWORD: String = "123456789123456"

        private const val SUCCESS: Boolean = true
        private const val FAILURE: Boolean = false
    }

    private lateinit var model: UsersModel


    @Before
    fun setup() {
        model = UsersModel(FakeUserProvider(SUCCESS))
    }

    @Test
    fun signUpUser_signupSuccess() = runTest {
        val response = model.signUpUser(VALID_USER_NAME, VALID_EMAIL, VALID_PASSWORD)
        Truth.assertThat(response).isTrue()
    }

    @Test
    fun signUpUser_emptyField_failure() = runTest {
        val response = model.signUpUser(EMPTY_STRING, VALID_EMAIL, VALID_PASSWORD)
        Truth.assertThat(response).isFalse()
    }

    @Test
    fun signUpUser_longPassword_failure() = runTest {
        val response = model.signUpUser(VALID_USER_NAME, VALID_EMAIL, INVALID_LONG_PASSWORD)
        Truth.assertThat(response).isFalse()
    }

    @Test
    fun signUpUser_providerFailure_failure() = runTest {
        val model = UsersModel(FakeUserProvider(FAILURE))
        val response = model.signUpUser(VALID_USER_NAME, VALID_EMAIL, VALID_PASSWORD)
        Truth.assertThat(response).isFalse()
    }

    @Test
    fun loginUser_loginSuccess() = runTest {
        val response = model.loginUser(VALID_USER_NAME, VALID_PASSWORD)
        Truth.assertThat(response).isTrue()
    }

    @Test
    fun loginUser_emptyField_failure() = runTest {
        val response = model.loginUser(EMPTY_STRING, VALID_PASSWORD)
        Truth.assertThat(response).isFalse()
    }

    @Test
    fun loginUser_providerFailure_failure() = runTest {
        val model = UsersModel(FakeUserProvider(FAILURE))
        val response = model.loginUser(VALID_USER_NAME, VALID_PASSWORD)
        Truth.assertThat(response).isFalse()
    }
}