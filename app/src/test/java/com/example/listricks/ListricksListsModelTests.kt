package com.example.listricks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.listricks.models.ListsModel
import com.example.listricks.providers.FakeListsProvider
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ListricksListsModelTests {

    @get:Rule
    val instantLiveDate = InstantTaskExecutorRule()

    private var listsModel: ListsModel = ListsModel(FakeListsProvider())

    @Before
    fun setup() {
        listsModel = ListsModel(FakeListsProvider())
        listsModel.setUserName(FIRST_USER_NAME)
        listsModel.setCurList(FIRST_COMBINE_LIST_NAME)
    }


    @Test
    fun setUserName() = runTest {
        listsModel.setUserName(FIRST_USER_NAME)
        Truth.assertThat(listsModel.getUserName()).isEqualTo(FIRST_USER_NAME)
    }

    @Test
    fun changeUserName() = runTest {
        listsModel.setUserName(FIRST_USER_NAME)
        listsModel.setUserName(SECOND_USER_NAME)
        Truth.assertThat(listsModel.getUserName()).isEqualTo(SECOND_USER_NAME)
    }

    @Test
    fun setListName() = runTest {
        listsModel.setCurList(FIRST_COMBINE_LIST_NAME)
        Truth.assertThat(listsModel.getCurList()).isEqualTo(FIRST_LIST_NAME)
    }

    @Test
    fun changeListName() = runTest {
        listsModel.setCurList(FIRST_COMBINE_LIST_NAME)
        listsModel.setCurList(SECOND_COMBINE_LIST_NAME)
        Truth.assertThat(listsModel.getCurList()).isEqualTo(SECOND_LIST_NAME)
    }


    companion object {
        private const val FIRST_USER_NAME = "Erez"
        private const val SECOND_USER_NAME = "Dor"

        private const val FIRST_LIST_NAME = "LIST-1"
        private const val SECOND_LIST_NAME = "LIST-2"

        private const val FIRST_COMBINE_LIST_NAME = FIRST_USER_NAME + "_" + FIRST_LIST_NAME
        private const val SECOND_COMBINE_LIST_NAME = SECOND_USER_NAME + "_" + SECOND_LIST_NAME
    }
}
