package com.example.listricks.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.listricks.R
import com.example.listricks.providers.ListsProvider
import com.example.listricks.viewModels.ListsViewModel


class AddProductFragment : Fragment(R.layout.add_products_fragment) {
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listsViewModel = ViewModelProvider(this,
            ListsViewModel.Factory { ListsProvider() }).get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setupViews()
    }

    private fun View.setupViews() {
        setupSearchBar()
    }

    private fun View.setupSearchBar() {
        val searchBar: SearchView = findViewById(R.id.search_bar)
        val previewTV: TextView = findViewById(R.id.newProductName)
        val addProductButton: ImageButton = findViewById(R.id.addSign)
        addProductButton.setOnClickListener {addProduct(searchBar, searchBar.query.toString())}

        searchBar.clearFocus()
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                addProduct(searchBar, query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                previewTV.text = newText
                return false
            }
        })
    }


    private fun addProduct(searchBar: SearchView, productName: String) {
        if (productName.isEmpty()){
            return
        }
        listsViewModel.addProduct(productName)
        searchBar.setQuery("", false)
    }
}
