package com.example.listricks.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listricks.R
import com.example.listricks.adapters.ProductsAdapter
import com.example.listricks.providers.ListsProvider
import com.example.listricks.viewModels.ListsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SingleListFragment : Fragment(R.layout.single_list_fragment) {
    private lateinit var listName: String
    private lateinit var progressBar: ProgressBar

    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listsViewModel = ViewModelProvider(this,
            ListsViewModel.Factory { ListsProvider() }).get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setupViews()
        listsViewModel.loadProducts()
        progressBar.isVisible = true
    }

    private fun View.setupViews() {
        listName = listsViewModel.getCurList()

        progressBar = findViewById(R.id.singleListProgressBar)
        val headline: TextView = findViewById(R.id.curListBar)
        headline.text = listName

        val addButton: FloatingActionButton = findViewById(R.id.addProductButton)
        addButton.setOnClickListener { navigateWithAction() }

        val shareButton: ImageButton = findViewById(R.id.shareListButton)
        shareButton.setOnClickListener { navigateToShareListFragment() }

        val deleteButton: ImageButton = findViewById(R.id.deleteList)
        deleteButton.setOnClickListener {listsViewModel.deleteList()}
        listsViewModel.deleteListLiveData.observe(viewLifecycleOwner) {
            navigateToListsGallery()
            deleteButton.isClickable = false
        }
        setupRecycler()
    }

    private fun View.setupRecycler() {
        val recycleView: RecyclerView = findViewById(R.id.products_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = ProductsAdapter().apply {
            listsViewModel.productsLiveData.observe(viewLifecycleOwner) { lst ->
                if (lst != null){
                    this.submitList(lst)
                    progressBar.isVisible = false
                }
            }
            setDeleteListener { productName -> listsViewModel.deleteProduct(productName) }
            setSelectClickListener { productName -> listsViewModel.selectProduct(productName) }
        }
    }

    private fun navigateWithAction() {
        val action = SingleListFragmentDirections.actionSingleListFragmentToAddProductFragment()
        findNavController().navigate(action)
    }

    private fun navigateToListsGallery() {
        val action =
            SingleListFragmentDirections.actionSingleListFragmentToListsGalleryFragment()
        findNavController().navigate(action)
    }

    private fun navigateToShareListFragment() {
        val action = SingleListFragmentDirections.actionSingleListFragmentToShareListFragment()
        findNavController().navigate(action)
    }
}