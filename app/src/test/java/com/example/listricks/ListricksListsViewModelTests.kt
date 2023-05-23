package com.example.listricks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.listricks.models.ListItem
import com.example.listricks.models.ProductItem
import com.example.listricks.providers.FakeListsProvider
import com.example.listricks.viewModels.ListsViewModel

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
class ListricksListsViewModelTests {

    @get:Rule
    val instantLiveDate = InstantTaskExecutorRule()

    private var listsViewModel: ListsViewModel = ListsViewModel(FakeListsProvider())

    @Before
    fun setup() {
        listsViewModel = ListsViewModel(FakeListsProvider())
        listsViewModel.setUserName(FIRST_USER_NAME)
        listsViewModel.setCurList(FIRST_COMBINE_LIST_NAME)
    }

    @Test
    fun setUserName() = runTest {
        listsViewModel = ListsViewModel(FakeListsProvider())
        listsViewModel.setUserName(FIRST_USER_NAME)
        Truth.assertThat(listsViewModel.getUserName()).isEqualTo(FIRST_USER_NAME)
    }

    @Test
    fun changeUserName() = runTest {
        listsViewModel = ListsViewModel(FakeListsProvider())
        listsViewModel.setUserName(FIRST_USER_NAME)
        listsViewModel.setUserName(SECOND_USER_NAME)
        Truth.assertThat(listsViewModel.getUserName()).isEqualTo(SECOND_USER_NAME)
    }

    companion object {
        private const val FIRST_USER_NAME = "Erez"
        private const val SECOND_USER_NAME = "Dor"

        private const val FIRST_LIST_NAME = "LIST-1"
        private const val SECOND_LIST_NAME = "LIST-2"

        private const val FIRST_COMBINE_LIST_NAME = FIRST_USER_NAME + "_" + FIRST_LIST_NAME
        private const val SECOND_COMBINE_LIST_NAME = SECOND_USER_NAME + "_" + SECOND_LIST_NAME

        private const val FIRST_ITEM_NAME = "ITEM-1"
        private const val SECOND_ITEM_NAME = "ITEM-2"

        private const val VALID_MAIL = "erez@gmail.com"
        private const val SUCCESS = true
    }

    @Test
    fun listsLiveData_defaultValue() = runTest {
        Truth.assertThat(listsViewModel.listsLiveData.value).isEqualTo(null)
    }

    @Test
    fun productsLiveData_defaultValue() = runTest {
        Truth.assertThat(listsViewModel.productsLiveData.value).isEqualTo(null)
    }

    @Test
    fun shareListLiveData_defaultValue() = runTest {
        Truth.assertThat(listsViewModel.shareListLiveData.value).isEqualTo(null)
    }

    @Test
    fun addListLiveData_defaultValue() = runTest {
        Truth.assertThat(listsViewModel.addListLiveData.value).isEqualTo(null)
    }

    @Test
    fun deleteListLiveData_defaultValue() = runTest {
        Truth.assertThat(listsViewModel.deleteListLiveData.value).isEqualTo(null)
    }

    @Test
    fun loadLists_checkEmptyAsDefault() = runTest {
        listsViewModel.loadLists()
        Truth.assertThat(listsViewModel.listsLiveData.value).isEqualTo(emptyList<ListItem>())
    }

    @Test
    fun addList_addSingleList() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        Truth.assertThat(listsViewModel.addListLiveData.value).isEqualTo(true)
    }

    @Test
    fun deleteList_addAndDeleteList() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.deleteList()
        Truth.assertThat(listsViewModel.deleteListLiveData.value).isEqualTo(true)
    }

    @Test
    fun setCurList() = runTest {
        listsViewModel.setCurList(FIRST_COMBINE_LIST_NAME)
        Truth.assertThat(listsViewModel.getCurList()).isEqualTo(FIRST_LIST_NAME)
    }

    @Test
    fun changeCurList() = runTest {
        listsViewModel.setCurList(FIRST_COMBINE_LIST_NAME)
        listsViewModel.setCurList(SECOND_COMBINE_LIST_NAME)
        Truth.assertThat(listsViewModel.getCurList()).isEqualTo(SECOND_LIST_NAME)
    }

    @Test
    fun loadProducts_checkEmptyAsDefault() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.loadProducts()
        Truth.assertThat(listsViewModel.productsLiveData.value).isEqualTo(emptyList<ProductItem>())
    }

    @Test
    fun addProduct_addSingleProduct() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.addProduct(FIRST_ITEM_NAME)
        Truth.assertThat(listsViewModel.productsLiveData.value).isEqualTo(
            listOf(
                ProductItem(
                    FIRST_ITEM_NAME, false
                )
            )
        )
    }

    @Test
    fun addProduct_addMultipleProduct() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.addProduct(FIRST_ITEM_NAME)
        listsViewModel.addProduct(SECOND_ITEM_NAME)
        Truth.assertThat(listsViewModel.productsLiveData.value).containsExactly(
            ProductItem(FIRST_ITEM_NAME, false), ProductItem(SECOND_ITEM_NAME, false)
        )
    }

    @Test
    fun deleteProduct_addAndDeleteProduct() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.addProduct(FIRST_ITEM_NAME)
        listsViewModel.addProduct(SECOND_ITEM_NAME)
        listsViewModel.deleteProduct(SECOND_ITEM_NAME)
        Truth.assertThat(listsViewModel.productsLiveData.value).isEqualTo(
            listOf(
                ProductItem(
                    FIRST_ITEM_NAME, false
                )
            )
        )
    }

    @Test
    fun selectProducts_addAndSelectProduct() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.addProduct(FIRST_ITEM_NAME)
        listsViewModel.selectProduct(FIRST_ITEM_NAME)
        Truth.assertThat(listsViewModel.productsLiveData.value).isEqualTo(
            listOf(
                ProductItem(
                    FIRST_ITEM_NAME, true
                )
            )
        )
    }

    @Test
    fun shareList_success() = runTest {
        listsViewModel.addList(FIRST_LIST_NAME)
        listsViewModel.shareList(VALID_MAIL)
        Truth.assertThat(listsViewModel.shareListLiveData.value).isEqualTo(SUCCESS)
    }
}



